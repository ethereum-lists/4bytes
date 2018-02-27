This is a collection of Ethereum Method signatures. It can be used to get the text-signatures from hex-signatures (with 4-bytes - hence the name)

A real world example:
```
> curl https://raw.githubusercontent.com/ethereum-lists/4bytes/master/signatures/a9059cbb
```

will result in this method (from the ERC-20 standard):

```
transfer(address,uint256)
```

If there are 2 methods matching this hex-signature they will be separated by semicolon(";").

To add your own signatures you can use the [4ByteBot](https://github.com/apps/4bytebot) - or enter them manually on the [4byte.directory](https://www.4byte.directory).
