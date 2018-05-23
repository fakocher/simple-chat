# Simple Chat

This is a simple chat created for the first lab of the Msc course Advanced Software Architecture and Design (ASAD) at the HES-SO, Lausanne.

## Usage

- Add the Java classes root path `<path-to-project>/out/production/simple-chat` to your environment variable `CLASSPATH`.
- Build the project (we used IntelliJ IDEA)
- Run `start rmiregistry`. It should open a blank CLI.
- Run the `Server` class with Java (from your favorite CLI or IDE). It should output `Server ready`.
- Run the `Client` class. It should output `Ready to chat!`. Test the API by typing the `hello` command.

## Client commands
- `signup <username> <password>` : to signup to server
- `login <username> <password>` : to login to server
- `connect <username>`: to join the chat group
- `showmembers`: to request and show the list of all connected members
- `requestchat <username>`: request a chat session with someone
- `message <message>`: send a message to the current chat session
- `quitsession`: quit the current chat session
- `disconnect`: to leave the chat group

## Known issues

- Cross-network usage not tested, only `localhost`
- The program should stop main input stream scanner when asking if user accepts session
- The user can start a session with hers/himself
- Can't make the `.jar` artifacts work, must run classes
- Error messages to be improved