# ZeroRPC Java Impelmentation

Mailing list: zerorpc@googlegroups.com (https://groups.google.com/d/forum/zerorpc)

zerorpc is a flexible RPC implementation based on zeromq and messagepack. Service APIs exposed with zerorpc are called "zeroservices".

This module aims to provide an implementation in Java that is compatible
with it's python and node.js counterpars.

## Create a Server

`org.zerorpc.Server` is a convenient wrapper that takes the target
service class to it's constructor. It then wraps it and proxies calls to
it that are recieved. 


```java

Service service = new Service<PersonService>(new PersonService());
service.bind("tcp://0.0.0.0:4242");
new Thread(service).start();


```



## Create a Client


```java

ZeroRPCClient client = new ZeroRPCClient();
client.connect("tcp://127.0.0.1:4242");

Integer id = client.invoke("add", 0, "James Carr", "james@zapier.com");

System.out.printf("Resulting id is %s", id);
Person p = client.invoke("get", new Person(), id.toString());
System.out.println(p.getEmail());

```


More to come. 
