<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- 为了在freemarker里面使用tile标签-->
<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<div>
    <@tiles.insertAttribute name="header"/>
</div>

<div>
Main page
</div>

<div>
    <@tiles.insertAttribute name="footer"/>
</div>
