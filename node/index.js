var server = require("./pruebanode");
var router = require("./router");

server.iniciar(router.route);