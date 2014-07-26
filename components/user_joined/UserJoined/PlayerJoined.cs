using System;
using System.Collections.Generic;
using System.Data.Odbc;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Npgsql;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace PlayerJoined
{
    public class PlayerJoined
    {
        static void Main(string[] args)
        {
            var factory = new ConnectionFactory() { HostName = "54.76.183.35", Port = 5672 };
            var postgreSql =
                new NpgsqlConnection(
                    "Server=microservices.cc9uedlzx2lk.eu-west-1.rds.amazonaws.com;Database=micro;User Id=microservices;Password=microservices;");

            postgreSql.Open();
            var rooms = ReadLocationsFromEventStore(postgreSql);
            postgreSql.Close();
            using (var connection = factory.CreateConnection())
            {
                using (var channel = connection.CreateModel())
                {
                    var consumer = new QueueingBasicConsumer(channel);
                    channel.QueueBind("user_joined", "alex2", "user_joined");
                    channel.BasicConsume("user_joined", true, consumer);
                    while (true)
                    {
                        var user = WaitForUserJoinedEvent(consumer);
                        PlacePlayerInRoom(user, rooms, connection);
                    }
                }
            }
        }

        private static void PlacePlayerInRoom(dynamic user, List<dynamic> rooms, IConnection connection)
        {
            var movement = new MovementSuccesfull
            {
                user_id = user.user_id,
                room_name = rooms.FirstOrDefault().room_name
            };
            var newMessage = JsonConvert.SerializeObject(movement);
            var newBody = Encoding.UTF8.GetBytes(newMessage);
            using (var movementChannel = connection.CreateModel())
            {
                movementChannel.BasicPublish("alex2", "movement_successful", null, newBody);
            }
        }

        private static dynamic WaitForUserJoinedEvent(QueueingBasicConsumer consumer)
        {
            var ea = (BasicDeliverEventArgs)consumer.Queue.Dequeue();
            var body = ea.Body;
            var message = Encoding.UTF8.GetString(body);
            Console.WriteLine(" [x] Received {0}", message);

            var user = JsonConvert.DeserializeObject<dynamic>(message);
            return user;
        }

        private static List<dynamic> ReadLocationsFromEventStore(NpgsqlConnection postgreSql)
        {
            var command = new NpgsqlCommand("Select content from facts where topic = 'room_created'", postgreSql);
            var jsons = new List<string>();
            var rooms = new List<dynamic>();
            using (var reader = command.ExecuteReader())
            {
                while (reader.Read())
                {
                    var json = reader.GetString(0);
                    jsons.Add(json);
                    var room = JsonConvert.DeserializeObject<dynamic>(json);
                    rooms.Add(room);
                }
            }
            Console.WriteLine("Read Locations:");
            foreach (var readJsons in jsons)
                Console.WriteLine(readJsons);
            return rooms;
        }
    }
}
