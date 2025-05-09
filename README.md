Ejecutar proyecto: 
Es una aplicacion web, el cual se tiene que clonar el proyecto en Springboot, se tiene que exportar la base de datos con el nombre hospital.sql, cambiar las credenciales respecto a su conexion ala base de datos en application.properties y finalmente ejecutar la aplicacion. 

Entrar con esta ruta ala aplicacion web = http://localhost:8080/citas

En la aplicacion se puede: 

1. Alta de cita (solicita los datos necesarios obligatorios)
  ● Las reglas para el alta son:
  ▪ No se puede agendar cita en un mismo consultorio a la misma hora.
  ▪ No se puede agendar cita para un mismo Dr. a la misma hora.
  ▪ No se puede agendar cita para un paciente a la una misma hora ni con menos de 2 horas
    de diferencia para el mismo día. Es decir, si un paciente tiene 1 cita de 2 a 3pm, solo
    podría tener otra el mismo día a partir de las 5pm.
   ▪ Un mismo doctor no puede tener más de 8 citas en el día.
2.  Consulta de citas.
  ▪ Puedes consultar por fecha, consultorio y por Dr. (Si soy el Dr. Ramos y quiero saber
  cuántas citas tengo para hoy o para mañana debe ser posible consultarlo en el sistema)
  ▪ Puedes cancelar una cita que aun este pendiente de realizarse según su horario.
  ▪ Puedes editar una cita tomando en cuenta las reglas de alta.
