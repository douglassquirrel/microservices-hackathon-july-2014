#! /usr/bin/env python

from json import dumps
from pika import BlockingConnection, ConnectionParameters
from sys import argv
from datetime import datetime

ROUTING_KEY = 'movement_successful_test'

print argv

host = '54.76.183.35'
connection = BlockingConnection(ConnectionParameters(host=host, port=5672))
channel = connection.channel()

time = str(datetime.utcnow())

product = {'player': argv[1], 'room': argv[2], 'time': time}
channel.basic_publish(exchange='alex2',
                      routing_key='movement_successful_test',
                      body=dumps(product))
print 'Published product %s' % (product,)
connection.close()
