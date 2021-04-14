<!DOCTYPE html>
<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]/>
<#assign dk=JspTaglibs["/WEB-INF/myTokenTaglib.tdl"]/>
<html>
<head>

</head>
<body>
this is hello world!
<@tiles.insertDefinition name="mainSimpleTemplate" />
<form action="submit.do"></form>
<@dk.myTokenTaglib id="mytoken"/>
<input type="text" value="${name}" name="name"/>
<input type="submit" value="submit"/>
<br/>
${getBytesSize("abcdefg")}
</body>
</html>