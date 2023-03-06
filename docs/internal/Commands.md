# Commands
When many commands are involved, certain standards must be established.

# Descriptions
Command descriptions are language-driven when enabled.\
To use language-driven descriptions, prepend a `$` to the description.

Otherwise, descriptions are displayed as plain text.

# Usage
Usage messages are auto-generated based on the provided arguments and sub-commands.\
They follow the conventions of:
- `[]` for optional arguments
- `<>` for required arguments
- `|` for multiple options
- `{}` for sub-commands

# Messages
Messages are an instance of `Text`, which is one of many things:
- The class can be language-driven.
- The class can be plain-text.
- The class is rich and can be formatted.

# Sub-Commands
Sub-commands handle logic for parts of a command.\

**Example**:\
`/account create` -> `CreateCommand.java` whereas:\
`/account delete` -> `DeleteCommand.java` but:\
`/account` -> `AccountCommand.java`

Commands get routed to the proper handler, depending on what's registered.\
If a "base command" (a command which isn't a type of sub-command) is executed, it will produce a usage message.

# Arguments
Arguments can be either:
- Ordered (the order in which you type them matters)
- Un-ordered (opposite of ordered; requires a prefix)

## Ordered
A command with ordered arguments is the default, and is what most commands use.\
The order in which you type the arguments matters.

**Example**:\
`/account create <username> <password> <email>` 

## Un-ordered
Un-ordered arguments are prefixed with a series of characters.\
The order in which they are typed does not matter.\
When seen in a usage command, they follow the format of:

`[(prefix)(Name)]` (the surrounding brackets will depend on the command)\
In all cases, the prefix will be **lowercase** while the name will be **uppercase**.\
There is also **no spacing** between the prefix and name.
