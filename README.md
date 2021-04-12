# ApacheKafka-BeginnerCourse

#Rodar Zookeeper e kafka no windows (pro linux/mac trocar o bat por sh); 
#obs: no windows pode ser necessário usar .\ no começo dos comandos

    zookeeper-server-start.bat config/zookeeper.Properties
    kafka-server-start.bat config/server.properties

#Alterando a quantidade de partições do kafka

    kafka-topics.bat --alter --zookeeper localhost:2181 --topic ECOMMERCE_NEW_ORDER --partitions 3

    vi config/server.properties