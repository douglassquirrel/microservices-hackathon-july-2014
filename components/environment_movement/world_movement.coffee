Array.prototype.last = ()->
    return this[-1..][0]

data_provider = require('./pg_connection')

mq_server = 'amqp://54.76.183.35:5672'
context = require('rabbit.js').createContext(mq_server);
exchange_name = 'alex2'

connected_rooms = []
users = {}

handlers = {
    'door_created': (message)->
        console.log(message)
        connected_rooms.push(message)
    'movement_successful': (message)->
        console.log(message)
        users[message.user_id].current_location = message.current_location
}

data_provider.getPreviousData( (data)->
    for row in data 
        handlers[row.topic]?(row.content)
)

context.on('ready', ->
    
    Object.keys(handlers).forEach((handler_name)->
        subscriber = context.socket('SUB',{routing:'topic'})
        subscriber.connect(exchange_name,handler_name,->
            console.log("#{handler_name} listener")
            subscriber.on('data',(string_message)->
                handlers[handler_name](JSON.parse(string_message))
            )
        )
    )
)