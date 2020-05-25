# hubitat-weewx-driver
This installs the skin needed to generate the **_daily.json_** for [Weewx with local capabilities](https://github.com/bdwilson/hubitat/tree/master/Weewx)

1. Download the Hubitat weeWX driver skin file master.zip<br>
   wget -O hubitat-weewx-driver.zip https://github.com/bdwilson/hubitat-weewx-driver/releases/download/v1.0/hubitat-weewx-driver.zip
1. wee_extension --install hubitat-weewx-driver.zip --- **_AS ROOT_**
1. Reload weeWX -- **service weewx reload** --- **_AS ROOT_**

To uninstall the extension:
1. wee_extension --uninstall hubitat-weewx-driver --- **_AS ROOT_**

Wait for the template to be generated. The file daily.json will generated in the HTML_ROOT defined in your weewx.conf

**!!NOTE!!** This works only for **Hubitat Elevation** And is not for general use with weeWX
