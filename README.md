# hubitat-weewx-driver
This installs the skin needed to generate the **_daily.json_** for [Cobra's Weewx Weather Driver With External Forecasting](https://community.hubitat.com/t/updated-weewx-weather-driver-with-external-forecasting/2226/143)

1. Download the Hubitat weeWX driver skin file master.zip<br>
   wget -O hubitat-weewx-driver.zip https://github.com/sgrayban/hubitat-weewx-driver/archive/master.zip
1. wee_extension --install hubitat-weewx-driver.zip
1. Reload weeWX -- **service weewx reload**

To uninstall the extension:
1. wee_extension --uninstall hubitat-weewx-driver

Wait for the template to be generated. The file daily.json will generated in the HTML_ROOT defined in your weewx.conf

**!!NOTE!!** This works only for **Hubitat Elevation** And is not for general use with weeWX
