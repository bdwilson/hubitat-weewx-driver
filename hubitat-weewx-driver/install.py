# installer for the Hubitat template
#
# 9th of Dec 2018

from setup import ExtensionInstaller

def loader():
    return DataInstaller()

class DataInstaller(ExtensionInstaller):
    def __init__(self):
        super(DataInstaller, self).__init__(
            version="1.0.0",
            name='hubitat-weewx-driver',
            description='A skin to feed data to the hubitat weewx driver app',
            author="Scott Grayban",
            author_email="sgrayban@gmail.com",
            config={
                'StdReport': {
                    'Hubitat': {
                        'skin':'Hubitat',
                        'HTML_ROOT':''}}},

            files=[('skins/Hubitat',
                    ['skins/Hubitat/daily.json.tmpl',
                     'skins/Hubitat/skin.conf'])
                   ]
            )

