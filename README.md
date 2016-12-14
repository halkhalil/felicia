# Felicia

Felicia is an accounting software. The major features include:

  - Invoices
  - Customers
  - Statistics

### Tech
   
* [Scala] - backend language
* [Play Framework] - backend framework
* [React] - frontend components framework
* [Twitter Bootstrap] - styles definitions
* [node.js] - frontend packages management
* [jQuery]


### Installation - development version

Clone repository:

```sh
$ git clone https://github.com/marcin-lawrowski/felicia.git
$ cd felicia
```

Edit conf/application.conf file, go to `db` section and provide valid database connection properties: `default.driver`, `default.url`, `default.username` and `default.password`. Run the application:

```sh
$ npm install -d
$ ./node_modules/.bin/webpack
$ sbt run
```
Open `http://localhost:9000/` address in web browser. Use the following credentials when you haven't created accounts yet: `admin`:`admin`.


License
----

GPL-3.0


**Free Software, Hell Yeah!**

[//]:#


   [Scala]: <https://www.scala-lang.org/>
   [Play Framework]: <https://www.playframework.com/>
   [React]: <https://facebook.github.io/react/>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
