context = require('rabbit.js').createContext('amqp://54.76.183.35:5672');
context.on('ready', ->
    pub = context.socket('PUB',{routing:'topic'}) 
    sub = context.socket('SUB',{routing:'topic'})
    
    
    
    sub.pipe(process.stdout);
    sub.connect('alex2','events', ->
        pub.connect('alex2', ->
            console.log('about to publish')
            pub.publish('events',JSON.stringify({welcome: 'rabbit.js'}), 'utf8');
        )
    )
)
