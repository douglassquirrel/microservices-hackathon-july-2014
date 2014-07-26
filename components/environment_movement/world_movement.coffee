data_provider = require('./pg_connection')

context = require('rabbit.js').createContext('amqp://54.76.183.35:5672');

connected_rooms = []
    
data_provider.getPreviousData( (data)->
        console.log(data)
        connected_rooms = data
)

context.on('ready', ->
    sub = context.socket('SUB',{routing:'topic'})
    context.exchange = 'alex2'
    sub.connect('alex2','door_created',->
        console.log('door_created listener')
    )
    sub.on('data',(message)->
        console.log(message)
        connected_rooms.push(JSON.parse(message))
    )
  
)