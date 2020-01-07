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

You can also get signatures with parameter names. Note that collisions will be higher here as people can use different parameter names and the parameter names do not change the signature. In the example before:

```
> curl https://raw.githubusercontent.com/ethereum-lists/4bytes/master/with_parameter_names/a9059cbb
```

will result in:

```
transfer(address _to,uint256 _value);transfer(address to,uint val)
```

So please also a plea to all smart contract developers. Please try to check if there are methods with similar signatures and think if you could use the same parameter names to have better UX in the consuming side.

To add your own signatures ideally verify your sourcecode via [verification.komputing.org](https://verification.komputing.org)
You can also use the [4ByteBot](https://github.com/apps/4bytebot) - or enter them manually on the [4byte.directory](https://www.4byte.directory). But verified source-code is the preferred option here.
