# VG::Logs::QueryDefinition

Creates or updates a query definition for CloudWatch Logs Insights.

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "VG::Logs::QueryDefinition",
    "Properties" : {
        "<a href="#loggroupnames" title="LogGroupNames">LogGroupNames</a>" : <i>[ String, ... ]</i>,
        "<a href="#name" title="Name">Name</a>" : <i>String</i>,
        "<a href="#querydefinitionid" title="QueryDefinitionId">QueryDefinitionId</a>" : <i>String</i>,
        "<a href="#querystring" title="QueryString">QueryString</a>" : <i>String</i>
    }
}
</pre>

### YAML

<pre>
Type: VG::Logs::QueryDefinition
Properties:
    <a href="#loggroupnames" title="LogGroupNames">LogGroupNames</a>: <i>
      - String</i>
    <a href="#name" title="Name">Name</a>: <i>String</i>
    <a href="#querydefinitionid" title="QueryDefinitionId">QueryDefinitionId</a>: <i>String</i>
    <a href="#querystring" title="QueryString">QueryString</a>: <i>String</i>
</pre>

## Properties

#### LogGroupNames

Use this parameter to include specific log groups as part of your query definition.

_Required_: No

_Type_: List of String

_Pattern_: <code>^[\.\-_/#A-Za-z0-9]+</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Name

A name for the query definition.

_Required_: Yes

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>255</code>

_Pattern_: <code>^([^:*\/]+\/?)*[^:*\/]+$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### QueryDefinitionId

If you are updating a query definition, use this parameter to specify the ID of the query definition that you want to update.

_Required_: No

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>256</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### QueryString

A collection of information that defines how metric data gets emitted.

_Required_: Yes

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>10000</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

## Return Values

### Ref

When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the QueryDefinitionId.
