<?xml version="1.0"?>
<recipe>

    <instantiate from="src/app_package/Contract.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${CLASS_NAME}Contract.kt" />

    <open file="${srcOut}/${CLASS_NAME}Contract.kt"/>
</recipe>