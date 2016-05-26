####Usage

To install library locally for the current user:

`make install`

To generate the cloud formation JSON file for default_vpc:

`make default_vpc.json`

To generate an SVG dependency diagram for AWS resources used by default_vpc:

`make default_vpc.svg`

To generate and display `default_vpc.svg` (requires feh):

`make viz`

(Branch [`viz`](../tree/viz) contains the output of `make viz`.)

To use in a downstream project:

```python
from troposphere import Template
import amazonia.default_vpc as default_vpc

template = new Template()
default_vpc.add_vpc(template,
    key_pair_name="your-ssh-key-name",
    nat_ip="nat.static.ip.address"
private_subnet = default_vpc.private_subnet(template)
template.add_resource(...)
template.add_resource(...)
print(template.to_json())
```
