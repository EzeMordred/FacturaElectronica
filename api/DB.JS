// DB.js
const mysql = require('mysql2');

class DB {
  constructor() {
    this.connection = mysql.createConnection({
      host: '34.134.141.129',
      user: 'root',
      password: '12345678',
      database: 'facturacion'
    });
  }

  connect() {
    this.connection.connect((err) => {
      if (err) {
        console.error('Error al conectar a la base de datos:', err);
        return;
      }

      console.log('Conexión exitosa a la base de datos MySQL');
    });
  }
}

module.exports = DB;

