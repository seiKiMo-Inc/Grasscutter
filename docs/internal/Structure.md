# Structure
Grasscutter-Rewrite is broken up into many packages.\
(this is to avoid disorganization and to make it easier to follow)

This document will elaborate on most packages and their purpose.\

# `io.grasscutter.utils`
**Path**: `src/main/java/io/grasscutter/utils`\
**Purpose**: Contains utility classes and methods.

**Structure**:
- `constants`: Constant properties & methods used throughout the project can be found here.
- `definitions`: Classes which define the structure of encoded data can be found here.
- `enums`: Enumerations used throughout the project can be found here.
- `interfaces`: Interfaces used throughout the project can be found here.
- `objects`: Classes with a general utility purpose which should be instantiated can be found here.
- `root`: Interfaces with static methods (file names generally end in `Utils`) can be found here.

# `io.grasscutter.server`
**Path**: `src/main/java/io/grasscutter/server`\
**Purpose**: Contains classes which are used for serving data.

**Structure**:
- `game`: Contains server-related classes for the **game** server. (UDP & KCP)
- `http`: Contains server-related classes for the **HTTP** server. (TCP & HTTP)
  - This package contains a subpackage for HTTP request routers.
- `root`: Contains classes which are used for managing the game & HTTP servers.

# `io.grasscutter.network`
**Path**: `src/main/java/io/grasscutter/network`\
**Purpose**: Contains classes with a purpose of handling network traffic.

**Structure**:
- `kcp`: Contains classes which are used in the game server.
  - These are internal classes for creating a KCP + Netty UDP server.
- `packets`: Contains packet handlers & objects.
- `protocol`: Contains classes for data handling and serialization.
- `root`: Contains classes used throughout the `network` package.