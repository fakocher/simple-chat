# Simple Chat

This is a simple chat created for the first lab of the Msc course Advanced Software Architecture and Design (ASAD) at the HES-SO, Lausanne.

## Usage

- Add the Java classes root path `<path-to-project>/out/production/simple-chat` to your environment variable `CLASSPATH`.
- Run `start rmiregistry`. It should open a blank CLI.
- Run the `Server` class with Java (from your favorite CLI or IDE). It should output `Server ready`.
- Run the `Client` class. It should output `Ready to chat!`. Test the API by typing the `hello` command.

## Client commands

- `connect <username>`: to join the chat group
- `disconnect`: to leave the chat group
- `memberlist`: to request and show the list of all connected members
- `requestchat <username>`: request a chat session with someone
- `message <message>`: send a message to the current chat session
- `quitsession`: quit the current chat session