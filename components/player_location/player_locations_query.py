#! /usr/bin/env python

from pika import BlockingConnection, ConnectionParameters
from psycopg2 import connect

RABBIT_MQ_HOST = '54.76.183.35'
RABBIT_MQ_PORT = 5672

QUERY_KEY = 'movement_successful_test'
ROUTING_KEY = 'where_is_everyone'

POSTGRES_HOST = 'microservices.cc9uedlzx2lk.eu-west-1.rds.amazonaws.com'
POSTGRES_DATABASE = 'micro'
POSTGRES_USER = 'microservices'
POSTGRES_PASSWORD = 'microservices'

def query(ch, method, properties, body):
    topic, content = method.routing_key, body
    conn = connect(host=POSTGRES_HOST, database=POSTGRES_DATABASE, 
                   user=POSTGRES_USER, password=POSTGRES_PASSWORD)
    cursor = conn.cursor()
    locations = cursor.execute('SELECT content->>player, content->>room FROM facts WHERE topic == QUERY_KEY;',
                   )
    cursor.close()
    conn.close()
    print 'Player, Room %s' % (locations,)


host = '54.76.183.35'
connection = BlockingConnection(ConnectionParameters(host=RABBIT_MQ_HOST, port=RABBIT_MQ_PORT))
channel = connection.channel()

user = {'user': "professor_green"}
channel.basic_publish(exchange='alex2',
                      routing_key=ROUTING_KEY,
                      body=user)
print 'Where is user %s' % (user,)
connection.close()