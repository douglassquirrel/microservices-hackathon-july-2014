Array.prototype.last = ()->
    return this[-1..][0]

data_provider = require('./pg_connection')

mq_server = 'amqp://54.76.183.35:5672'
context = require('rabbit.js').createContext(mq_server);
exchange_name = 'alex2'
    
connected_rooms = []
user = {}
    
data_provider.getPreviousData( (data)->
    
    
    connected_rooms  = data.filter((row)-> row.topic == 'door_created')
                        .map((row)-> row.content )
    
    user.current_location = data.filter((row)-> row.topic == 'movement_successful')
                                .map((row)-> row.content.room_name)
                                .last()
                                
    console.log(connected_rooms)
)

context.on('ready', ->
    door_created_sub = context.socket('SUB',{routing:'topic'})
    
    door_created_sub.connect(exchange_name,'door_created',->
        console.log('door_created listener')
        door_created_sub.on('data',(message)->
            console.log(message)
            connected_rooms.push(JSON.parse(message))
        )
    )


    movement_successful_sub = context.socket('SUB',{routing:'topic'})
    movement_successful_sub.connect(exchange_name,'movement_successful',->
        console.log('movement_successful listener')
        movement_successful_sub.on('data',(message)->
            message = JSON.parse(message)
            user.current_location = message.current_location
        )
    )
    
)