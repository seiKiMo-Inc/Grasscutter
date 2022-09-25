### Thank you for your interest in the _Anime Game_ protocol!
### For anyone more knowledgeable, please consider contributing.

# Table of Contents
- [Table of Contents](#table-of-contents)
- [Protocol Specifications](#protocol-specifications)
- [Connecting to Servers](#connecting-to-servers)
- [The Seed Key](#the-seed-key)

# Protocol Specifications
_Anime Game_ uses the following technologies:
- [Protocol Buffers](https://developers.google.com/protocol-buffers) for data serialization.
- [KCP](https://github.com/skywind3000/kcp) for a reliable data transmission protocol.
  - _Anime Game_ actually uses a slightly modified version.
  - "An unsigned 32-bit integer field is added between `conv` and `cmd` in the header."
  - Paraphrased from [boba-ps/kcp-ts](https://github.com/boba-ps/kcp-ts).
- [XOR](https://en.wikipedia.org/wiki/XOR_cipher) for encrypting messages.

# Connecting to Servers
_trust me, this is relevant to the protocol._

When connecting to a server for the first time, the client performs an _MT19937_64 key exchange_.\
The process is as follows:
1. A client connects to a server.
    1. A KCP handshake is performed during this time.
2. The client sends a `GetPlayerTokenReq` message to the server.
    1. This packet is encrypted with the _dispatch*_ key.
3. The server responds with a `GetPlayerTokenRsp` message.
    1. This packet is encrypted with the dispatch key.
    2. This is the last packet which is encrypted with the dispatch key.

After this, the client and server use the _session_ key for all further communication.\
The session key is generated using the MT19937_64 algorithm, where a seed is taken and a key is generated.\
The seed used is the `secretKeySeed` field of the `GetPlayerTokenRsp` message sent by the server.

For the purposes of Grasscutter, the `secretKeySeed` field is **hardcoded**, so the session key never has to be regenerated.\
This behavior varies across server software authors. Some servers generate a random seed for each connection.

*: The "dispatch key" is what Grasscutter calls the key internally. 
This is the initial key used for pre-exchange messages and used in decrypting dispatch server responses.

# The Seed Key
Prior to game version _2.7.50_, the seed key was a plain-text integer.\
Since game version _2.7.50_, the seed key is encrypted with an RSA private key*.

*: As of writing, I am unsure of this point. Please change if necessary.