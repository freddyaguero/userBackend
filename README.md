Prerequisitos:
--------------
Tener instalado el siguiente software:
1. Java Development Kit (JDK) versión 8 o 11
    Verificar en una ventana de comandos con:
    java -version
2. Gradle:
    Verifica con:
    gradle -v 
3. Git
    Verifica con:
    git --version
4. Herramienta: Postman

Nota:Este proyecto fue desarrollado con Visual Studio Code.


Clonar el proyecto desde el repositorio GITHUB
----------------------------------------------
En una ventana de comandos.
1. Crear una carpeta donde se desea clonar el proyecto:
   mkdir proyecto
2. Entrar a la carpeta:
   cd proyecto
3. Clonar proyecto
    git clone https://github.com/freddyaguero/userBackend.git
4. Entrar a la carpeta del proyecto clonado:
   cd userBackend
5. Verificar que estén todos los archivos del proyecto
   dir


Ejecutar test, Construir y ejecutar el proyecto con gradle
-------------------------------------------
Continuando en la ventana de comandos anterior
1. Ejecutar test:
    gradle test
2. Construir el proyecto con gradle: 
    gradle build
3. Ejecutar el proyecto
java -jar build/libs/userbackend-0.0.1-SNAPSHOT.jar

Probar los servicios con una herramienta como POSTMAN
-----------------------------------------------------
1. Se dejan los collection de prueba en la carpeta: postman


Verificación de la base de datos H2
-----------------------------------
1. En el navegador Chorme ir a la URL:
http://localhost:8080/h2-console

2. Llenar los siguientes valores en la página:
Save Setting: Generic H2 (Embedded)
Save name: Generic H2 (Embedded)
Driver class: org.h2.Driver
JDBC URL: jdbc:h2:mem:userbackenddb
User Name:sa

3. Presionar el botón CONNECT


Diagramas
---------
1. Carpeta: diagramas