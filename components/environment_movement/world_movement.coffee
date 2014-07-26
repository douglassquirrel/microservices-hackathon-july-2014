Array.prototype.last = ()->
    return this[-1..][0]

data_provider = require('./pg_connection')

mq_server = 'amqp://54.76.183.35:5672'
context = require('rabbit.js').createContext(mq_server);
exchange_name = 'alex2'

connected_rooms = []
users = {}

publish_movement_success = (user_id,room)->
    console.log('Ready to publish success')
    pub = context.socket('PUB',{routing:'topic'});

    pub.on('error',(err)-> console.log('Crashed',err))
    
    pub.connect('alex2',->
        console.log('Publishing movement_successful for',user_id)
        pub.publish('movement_successful',JSON.stringify(
            {
            user_id:user_id,
            room_name:room
            }
        ));
    )

publish_movement_failed = (user_id,room)->
    console.log('Ready to publish failure')
    pub = context.socket('PUB',{routing:'topic'});
    
    pub.on('error',(err)-> console.log('Crashed',err))
    
    pub.connect('alex2',->
        console.log('Publishing movement_failed for',user_id)
        pub.publish('movement_failed',JSON.stringify(
            {
            user_id:user_id,
            room_name:room
            }
        ));
    )

handlers = {
    'door_created': (message)->
        console.log('door_created :',message)
        connected_rooms.push([message.room_one_name, message.room_two_name])
        
    'movement_successful': (message)->
        console.log('movement_successful :', message)
        users[message.user_id] = users[message.user_id] || {}
        users[message.user_id].current_location = message.room_name
        
    'user_intended_to_move' : (message,isReplay)->
        if isReplay then return 
            
        console.log('user_intended_to_move :',message)
        user = users[message.user_id]
        if not user 
            console.log('\tuser does not exist')
            return
        
        location = user.current_location
        for door in connected_rooms
            if location in door and message.room_id in door
                publish_movement_success(message.user_id,message.room_id)
                return;
        publish_movement_failed(message.user_id,message.room_id)
}

context.on('ready', ->
    data_provider.getPreviousData( (data)->
        for row in data
            handlers[row.topic]?(row.content,true)
    )

#    console.log('\n****ready to listen for new stuff****\n')

    Object.keys(handlers).forEach((topic_name)->
        subscriber = context.socket('SUB',{routing:'topic'})
        subscriber.connect(exchange_name,topic_name,->
            console.log("#{topic_name} listener started")
            subscriber.on('data',(string_message)->
                handlers[topic_name](JSON.parse(string_message))
            )
        )
    )
)