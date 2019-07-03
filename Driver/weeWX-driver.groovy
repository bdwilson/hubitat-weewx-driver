/**
 * Please Note: This app is NOT released under any open-source license.
 * Please be sure to read the license agreement before installing this code.
 *
 *  Weewx Weather Driver for Hubitat Elevation
 *
 *  Copyright © 2019 AScott Grayban
 *  Original code from Andrew Parker
 *
 * This software package is created and licensed by Scott Grayban.
 *
 * This software, along with associated elements, including but not limited to online and/or electronic documentation are
 * protected by international laws and treaties governing intellectual property rights.
 *
 * This software has been licensed to you. All rights are reserved. You may use and/or modify the software.
 * You may not sublicense or distribute this software or any modifications to third parties in any way.
 *
 * You may not distribute any part of this software without the author's express permission
 *
 * By downloading, installing, and/or executing this software you hereby agree to the terms and conditions set forth in the Software license agreement.
 * This agreement can be found on-line at: https://sgrayban.github.io/Hubitat-Public/software_License_Agreement.txt
 * 
 * Hubitat is the trademark and intellectual property of Hubitat Inc. 
 * Scott Grayban has no formal or informal affiliations or relationships with Hubitat.
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License Agreement
 * for the specific language governing permissions and limitations under the License.
 *
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  This driver is specifically designed to be used with 'Weewx' and your own PWS and
 *  requires you to install my Weewx Skin at https://github.com/sgrayban/hubitat-weewx-driver/
 *
 *  Version:
 *  1.0.0 - Initial commit
 *
 */

metadata {
    definition (
	name: "Weewx Weather Driver",
	namespace: "sgrayban",
	author: "Scott Grayban",
	importUrl: "https://raw.githubusercontent.com/sgrayban/hubitat-weewx-driver/master/Driver/weeWX-driver.groovy"
	)

	{
        capability "Actuator"
        capability "Sensor"
        capability "Temperature Measurement"
        capability "Illuminance Measurement"
        capability "Relative Humidity Measurement"
        capability "Polling"
        command "PollStation"
        command "poll"
        
// Base Info   
        attribute "Missing or No Data", "string"
        attribute "WeewxUptime", "string"
        attribute "WeewxLocation", "string"
        attribute "Refresh-Weewx", "string"
	attribute "WeatherDisplay", "string"
        
// Units
        attribute "distanceUnit", "string"
        attribute "pressureUnit", "string"
        attribute "rainUnit", "string"
        
// Collected Local Station Data       
        attribute "solarradiation", "string"
        attribute "dewpoint", "string"
        attribute "inside_humidity", "string"
        attribute "inside_temperature", "string"
        attribute "pressure", "string"
        attribute "pressure_trend", "string"
        attribute "wind", "string"
        attribute "wind_gust", "string"
        attribute "maxwindGust", "string"
        attribute "wind_dir", "string"
        attribute "rain_rate", "string"
        attribute "uv", "string"
        attribute "uvHarm", "string"
        attribute "feelsLike", "string"
        attribute "LastUpdate-Weewx", "string"
        attribute "precip_1hr", "string"
        attribute "precip_today", "string"
        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "tempMaxToday", "string"
        attribute "tempMinToday", "string"
        attribute "tempMaxInsideToday", "string"
        attribute "tempMinInsideToday", "string"
    }

    preferences() {
    
        section("Query Inputs"){
            input "ipaddress", "text", required: true, title: "weeWX Server IP/URI", defaultValue: "0.0.0.0"
            input "weewxPort", "text", required: true, title: "Connection Port", defaultValue: "800"
            input "weewxPath", "text", required: true, title: "path to file", defaultValue: "weewx/daily.json"
            input "unitSet", "bool", title: "Display Data Units", required: true, defaultValue: false
            input "logSet", "bool", title: "Log All Data", required: true, defaultValue: false
            input "pollInterval", "enum", title: "weeWX Station Poll Interval", required: true, defaultValue: "5 Minutes", options: ["Manual Poll Only", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
            input "pressureUnit", "enum", title: "Pressure Unit", required:true, defaultValue: "INHg", options: ["INHg", "MBAR"]
            input "rainUnit", "enum", title: "Rain Unit", required:true, defaultValue: "IN", options: ["IN", "MM"]
            input "speedUnit", "enum", title: "Distance & Speed Unit", required:true, defaultValue: "Miles (MPH)", options: ["Miles (MPH)", "Kilometers (KPH)"]
            input "temperatureUnit", "enum", title: "Temperature Unit", required:true, defaultValue: "Fahrenheit (�F)", options: ["Fahrenheit (�F)", "Celsius (�C)"]
            input "decimalUnit", "enum", title: "Max Decimal Places", required:true, defaultValue: "2", options: ["1", "2", "3", "4", "5"]
        }
	
		section(){
		input "summaryDashboard", "bool", title: "Weather Dashboard Summary", required: true, defaultValue: false, submitOnChange: true
			if(summaryDashboard == true){
			input "fweight", "enum",  title: "Font Weight", submitOnChange: true, defaultValue: "Normal", options: ["Normal", "Italic", "Bold"]
			input "fcolour", "text",  title: "Font Colour (Hex Value)", defaultValue:"000000", submitOnChange: true
	//		input "fsize", "enum",  title: "Font Size", submitOnChange: true, defaultValue: "Normal", options: fontSize()
			
		input "dashboardFormat", "bool", title: "Custom Weather Dashboard Summary", required: true, defaultValue: false, submitOnChange: true		
			if(dashboardFormat == true){
			input "slot1", "enum",  title: "Attribute For Line 1", options: checkInput(), submitOnChange: true		
					if(slot1 == "Free Text"){ 
					input "slot1Text1", "text",  title: "Text For Line 1", submitOnChange: true	
					}	
			input "slot2", "enum",  title: "Attribute For Line 2", options: checkInput()
					if(slot2 == "Free Text"){ 
					input "slot2Text1", "text",  title: "Text For Line 2", submitOnChange: true	
					}	
			input "slot3", "enum",  title: "Attribute For Line 3", options: checkInput()
					if(slot3 == "Free Text"){ 
					input "slot3Text1", "text",  title: "Text For Line 3", submitOnChange: true	
					}	
			input "slot4", "enum",  title: "Attribute For Line 4", options: checkInput()
					if(slot4 == "Free Text"){ 
					input "slot4Text1", "text",  title: "Text For Line 4", submitOnChange: true	
					}	
			input "slot5", "enum",  title: "Attribute For Line 5", options: checkInput()
					if(slot5 == "Free Text"){ 
					input "slot5Text1", "text",  title: "Text For Line 5", submitOnChange: true	
					}	
			input "slot6", "enum",  title: "Attribute For Line 6", options: checkInput()
					if(slot6 == "Free Text"){ 
					input "slot6Text1", "text",  title: "Text For Line 6", submitOnChange: true	
					}
			input "slot7", "enum",  title: "Attribute For Line 7", options: checkInput()
					if(slot7 == "Free Text"){ 
					input "slot7Text1", "text",  title: "Text For Line 7", submitOnChange: true	
					}	
			input "slot8", "enum",  title: "Attribute For Line 8", options: checkInput()
					if(slot8 == "Free Text"){ 
					input "slot8Text1", "text",  title: "Text For Line 8", submitOnChange: true	
					}			
				}
			}
		}
    }
}

def initialize(){
    updated()
}

private dbCleanUp() {
	unschedule()
	state.remove("Missing or No Data")
//	state.remove("icon")
//	state.remove("Copyright")
//	state.remove("author")
//	state.remove("InternalName")
//	state.remove("newUpdateDate")
//	state.remove("Status")
//	state.remove("UpdateInfo")
//	state.remove("CobraAppCheck")
}

def updated() {
    dbCleanUp()
    sendEvent(name: "Missing or No Data", value: "---")
    log.debug "Updated called"
    logCheck()
    units()
    PollStation()
    def pollIntervalCmd = (settings?.pollInterval ?: "3 Hours").replace(" ", "")
    if(pollInterval == "Manual Poll Only"){LOGINFO( "MANUAL POLLING ONLY")}
    else{ "runEvery${pollIntervalCmd}"(pollSchedule)}
}

def poll(){
    log.info "Manual Poll"
    PollStation()
	if(summaryDashboard == true){
	if(dashboardFormat == true){advancedDash()}
	if(dashboardFormat == false){standardDash()}
	}
	if(summaryDashboard == false){sendEvent(name: "WeatherDisplay", value: "N/A ")}
}

def units(){
    state.SRU = " watts"
    state.IU = " watts"
 	state.HU = " %" 
    
    state.DecimalPlaces = decimalUnit.toInteger()
    state.DisplayUnits = unitSet
}

def pollSchedule()
{
    PollStation()
	if(summaryDashboard == true){
	if(dashboardFormat == true){advancedDash()}
	if(dashboardFormat == false){standardDash()}
	}
	if(summaryDashboard == false){sendEvent(name: "WeatherDisplay", value: "N/A ")}
}
              
def parse(String description) {
}

def PollStation()
{
    units()
    LOGDEBUG("Weewx: ForcePoll called")
    def params1 = [
        uri: "http://${ipaddress}:${weewxPort}/${weewxPath}"
         ]
    
    try {
        httpGet(params1) { resp1 ->
            resp1.headers.each {
            LOGINFO( "Response1: ${it.name} : ${it.value}")
        }
            if(logSet == true){  
           
            LOGINFO( "params1: ${params1}")
            LOGINFO( "response contentType: ${resp1.contentType}")
 		    LOGINFO( "response data: ${resp1.data}")
            } 

            if(logSet == false){ 
      //      log.info "Further Weewx detailed data logging disabled"    
            }    
            
// Collect Data
           
 // ************************ ILLUMINANCE **************************************************************************************           
           LOGINFO("Checking illuminance")    
            def illuminanceRaw = (resp1.data.stats.current.solarRadiation)  
                if(illuminanceRaw == null || illuminanceRaw.contains("N/A")){
                	state.Illuminance = '---'
                }   
            	else{
                	def illuminanceRaw1 = (resp1.data.stats.current.solarRadiation.replaceFirst(wmcode, ""))
                	state.Illuminance = illuminanceRaw1.toFloat()
                }
            
// ************************* SOLAR RADIATION*****************************************************************************************           
            	LOGINFO("Checking SolarRadiation")
              def solarradiationRaw = (resp1.data.stats.current.solarRadiation)
            	if(solarradiationRaw == null || solarradiationRaw.contains("N/A")){
                  	state.SolarRadiation = '---'
                }
            	else{
                     def solarradiationRaw1 = (resp1.data.stats.current.solarRadiation.replaceFirst(wmcode, ""))
                     state.SolarRadiation = solarradiationRaw1.toFloat()
                }
            
// ************************** HUMIDITY ***************************************************************************************   
         LOGINFO("Checking Humidity")
              def humidityRaw = (resp1.data.stats.current.humidity)
            	if(humidityRaw == null || humidityRaw.contains("N/A")){
                state.Humidity = '---'
                }
            	else{
                   def humidityRaw1 = (resp1.data.stats.current.humidity.replaceFirst("%", ""))
                   state.Humidity = humidityRaw1
                }

// ************************** INSIDE HUMIDITY ************************************************************************************
            LOGINFO("Checking Inside Humidity")
              def inHumidRaw1 = (resp1.data.stats.current.insideHumidity.replaceFirst("%", "")) 
            	if(inHumidRaw1 ==null || inHumidRaw1.contains("N/A")){
                   
                	state.InsideHumidity = '---'}
            	else{
                    
                	state.InsideHumidity = inHumidRaw1
                }
                        
            
// ************************* DEWPOINT *****************************************************************************************
            LOGINFO("Checking Dewpoint")
                def dewpointRaw1 = (resp1.data.stats.current.dewpoint)
                 	if(dewpointRaw1 == null || dewpointRaw1.contains("N/A")){
                    state.Dewpoint = '---'}
            
            	if (dewpointRaw1.contains("F")) {
                dewpointRaw1 = dewpointRaw1.replace(fcode, "")
                    
                if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                state.Dewpoint = dewpointRaw1
                LOGINFO("Dewpoint Input = F - Output = F -- No conversion required")
                }    
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                def dewpoint1 = convertFtoC(dewpointRaw1) 
                state.Dewpoint = dewpoint1 
                   
                }    
            } 
            
           		if (dewpointRaw1.contains("C")) {
                dewpointRaw1 = dewpointRaw1.replace(ccode, "")
                    
                 if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                def dewpoint1 = convertCtoF(dewpointRaw1)    
                state.Dewpoint = dewpoint1 
                }    
                 if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                state.Dewpoint = dewpointRaw1
                 LOGINFO("Dewpoint Input = C - Output = C -- No conversion required" ) 
                }        
            } 

// ************************** PRESSURE ****************************************************************************************            
           LOGINFO("Checking Pressure")
              def pressureRaw1 = (resp1.data.stats.current.barometer)
                    if (insideTemperatureRaw1 == null || pressureRaw1.contains("N/A")){
                    state.Pressure = '---'}
            
            if (pressureRaw1.contains("inHg")) {
                pressureRaw1 = pressureRaw1.replace("inHg", "")
                
                if(pressureUnit == "INHg"){
            	state.PU = 'inhg'
                state.Pressure = pressureRaw1
                LOGINFO("Pressure Input = INHg - Output = INHg -- No conversion required")
                }
                
                if(pressureUnit == "MBAR"){
                state.PU = 'mbar'
                def pressureTemp1 = convertINtoMB(pressureRaw1) 
                state.Pressure = pressureTemp1 
                
                }
            } 
            
            if (pressureRaw1.contains("mbar")) {
                 pressureRaw1 = pressureRaw1.replace("mbar", "")
                
            	if(pressureUnit == "INHg"){
            	state.PU = 'inhg'
                def pressureTemp1 = convertMBtoIN(pressureRaw1)
                state.Pressure = pressureTemp1
                }
                 if(pressureUnit == "MBAR"){
                 state.PU = 'mbar'
                 state.Pressure = pressureRaw1 
                 LOGINFO( "Pressure Input = MBAR - Output = MBAR --No conversion required")
                }
            } 
            
// ************************** WIND SPEED ****************************************************************************************
            LOGINFO("Checking Wind speed")
    		  def windSpeedRaw1 = (resp1.data.stats.current.windSpeed) 
            if(windSpeedRaw1 == null || windSpeedRaw1.contains("N/A")){
                    state.WindSpeed = '---'}
            
            if (windSpeedRaw1.contains("mph")) {
                windSpeedRaw1 = windSpeedRaw1.replace("mph", "")
                
                if(speedUnit == "Miles (MPH)"){
            	state.SU = 'mph'
                state.WindSpeed = windSpeedRaw1
                LOGINFO("Wind Speed Input = MPH - Output = MPH -- No conversion required")
                }
                
                if(speedUnit == "Kilometers (KPH)"){
                state.SU = 'kph'
                def speedTemp1 = convertMPHtoKPH(windSpeedRaw1) 
                state.WindSpeed = speedTemp1 
            
                }
                
            } 
            
            if (windSpeedRaw1.contains("km/h")) {
                 windSpeedRaw1 = windSpeedRaw1.replace("km/h", "")
                
            	if(speedUnit == "Miles (MPH)"){
            	state.SU = 'mph'
                def speedTemp1 = convertKPHtoMPH(pressureRaw1)
                state.WindSpeed = speedTemp1
                }
                 if(speedUnit == "Kilometers (KPH)"){
                 state.SU = 'kph'
                 state.WindSpeed = windSpeedRaw1 
                 LOGINFO("WindSpeed Input = KPH - Output = KPH --No conversion required")
                }
                
            } 
            
                   
// ************************** WIND GUST ****************************************************************************************
            LOGINFO("Checking Wind Gust")
              def windGustRaw1 = (resp1.data.stats.current.windGust)  
            	 if(windGustRaw1 == null || windGustRaw1.contains("N/A")){
                    state.WindGust = '---'}
            
            if (windGustRaw1.contains("mph")) {
                windGustRaw1 = windGustRaw1.replace("mph", "")
                
                if(speedUnit == "Miles (MPH)"){
            	state.SU = 'mph'
                state.WindGust = windGustRaw1
                LOGINFO( "Wind Gust Speed Input = MPH - Output = MPH -- No conversion required")
                }
                
                if(speedUnit == "Kilometers (KPH)"){
                state.SU = 'kph'
                def speedTemp2 = convertMPHtoKPH(windGustRaw1) 
                state.WindGust = speedTemp2 
            
                }
                
            } 
            
            if (windGustRaw1.contains("km/h")) {
                 windGustRaw1 = windGustRaw1.replace("km/h", "")
                
            	if(speedUnit == "Miles (MPH)"){
            	state.SU = 'mph'
                def speedTemp2 = convertKPHtoMPH(windGustRaw1)
                state.WindGust = speedTemp2
                }
                 if(speedUnit == "Kilometers (KPH)"){
                 state.SU = 'kph'
                 state.WindGust = windGustRaw1 
                LOGINFO( "Wind Gust Speed Input = KPH - Output = KPH --No conversion required")
                }
                
            } 
            
// ************************** INSIDE TEMP **************************************************************************************** 
          LOGINFO("Checking Inside Temperature")
              def insideTemperatureRaw1 = (resp1.data.stats.current.insideTemp)
                    if (insideTemperatureRaw1 == null || insideTemperatureRaw1.contains("N/A")){
                    state.InsideTemp = '---'}
            
            if (insideTemperatureRaw1.contains("F")) {
                insideTemperatureRaw1 = insideTemperatureRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                state.InsideTemp = insideTemperatureRaw1
                LOGINFO("InsideTemperature Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                def insideTemp1 = convertFtoC(insideTemperatureRaw1) 
                state.InsideTemp = insideTemp1 
                
                }
                
            } 
            
            if (insideTemperatureRaw1.contains("C")) {
                insideTemperatureRaw1 = insideTemperatureRaw1.replace(ccode, "")
                
            	if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                def insideTemp1 = convertCtoF(insideTemperatureRaw1)
                state.InsideTemp = insideTemp1
                }
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                state.InsideTemp = insideTemperatureRaw1  
                LOGINFO( "InsideTemperature Input = C - Output = C --No conversion required")
                }
                
            } 
  
// ************************** RAIN RATE ****************************************************************************************    
            LOGINFO("Checking Rain Rate")
            
            def rainRateRaw1 = (resp1.data.stats.current.rainRate) 
            	if(rainRateRaw1 == null || rainRateRaw1.contains("N/A")){
                   state.Rainrate = '---'}
            	
            if(rainRateRaw1.contains("in/hr")){
                rainRateRaw1 = rainRateRaw1.replace("in/hr", "")
                
                if(rainUnit == "IN"){
                    state.RRU = " in/hr"
                 	state.Rainrate = rainRateRaw1  
                    LOGINFO( "Rainrate Input = in/hr - Output = in/hr --No conversion required")
                }
            
            	if(rainUnit == "MM"){
            		state.RRU = "mm/hr"
                    rrTemp = convertINtoMM(rainRateRaw1)
                    state.Rainrate = rrTemp
            }
            }
            
             if(rainRateRaw1.contains("mm/hr")){
                rainRateRaw1 = rainRateRaw1.replace("mm/hr", "")
                0.621371
                if(rainUnit == "IN"){
                    state.RRU = "in/hr"
                    rrTemp = convertMMtoIN(rainRateRaw1)
                 	state.Rainrate = rrTemp 
                }
            
            	if(rainUnit == "MM"){
            		state.RRU = " mm/hr"
                   state.Rainrate = rainRateRaw1 
                   LOGINFO( "Rainrate Input = mm/hr - Output = mm/hr --No conversion required")
            }
            }
            

// ************************** RAIN TODAY ****************************************************************************************    
            LOGINFO("Checking Rain Today")
              def rainTodayRaw1 = (resp1.data.stats.sinceMidnight.rainSum)
               	if(rainTodayRaw1 == null || rainTodayRaw1.contains("N/A")){
                   state.RainToday = '---'}
            	
            if(rainTodayRaw1.contains("in")){
                rainTodayRaw1 = rainTodayRaw1.replace("in", "")
                
                if(rainUnit == "IN"){
                    state.RU = "in"
                 	state.RainToday = rainTodayRaw1 
                    LOGINFO( "RainToday Input = in - Output = in --No conversion required")
                }
            
            	if(rainUnit == "MM"){
            		state.RU = "mm"
                    rtTemp = convertINtoMM(rainTodayRaw1)
                    state.RainToday = rtTemp
            }
            }
            
             if(rainTodayRaw1.contains("mm")){
                rainTodayRaw1 = rainTodayRaw1.replace("mm", "")
                
                if(rainUnit == "IN"){
                    state.RU = "in"
                    rtTemp = convertMMtoIN(rainTodayRaw1)
                 	state.RainToday = rtTemp 
                }
            
            	if(rainUnit == "MM"){
            		state.RU = "mm"
                   state.RainToday = rainTodayRaw1 
                  LOGINFO("RainToday Input = mm - Output = mm --No conversion required")
            }
            }


// ************************** TEMPERATURE ****************************************************************************************
            LOGINFO("Checking Temperature")
              def temperatureRaw1 = (resp1.data.stats.current.outTemp) 
            	if(temperatureRaw1 ==null || temperatureRaw1.contains("N/A")){
                state.Temperature = '---'}
            
            if (temperatureRaw1.contains("F")) {
                temperatureRaw1 = temperatureRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                state.Temperature = temperatureRaw1
                LOGINFO("Temperature Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                def temp1 = convertFtoC(temperatureRaw1) 
                state.Temperature = temp1 
                
                }
                
            } 
            
            if (temperatureRaw1.contains("C")) {
                temperatureRaw1 = temperatureRaw1.replace(ccode, "")
                
            	if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                    def temp1 = convertCtoF(temperatureRaw1)
                state.Temperature = temp1
                }
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                state.Temperature = temperatureRaw1  
                 LOGINFO("Temperature Input = C - Output = C --No conversion required")
                }
                
            }
           
                    
// ************************** MIN Outside TEMPERATURE *******************************************************************************                     

             LOGINFO("Checking Min Outside Temperature")
              def tempMinRaw1 = (resp1.data.stats.sinceMidnight.mintemptoday) 
            	if(tempMinRaw1 ==null || tempMinRaw1.contains("N/A")){
                state.MinTemperature = '---'}
            
            if (tempMinRaw1.contains("F")) {
                tempMinRaw1 = tempMinRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                state.MinTemperature = tempMinRaw1
                LOGINFO("Min Temperature Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                def tempMin = convertFtoC(tempMinRaw1) 
                state.MinTemperature = tempMin 
                
                }
                
            } 
            
            if (tempMinRaw1.contains("C")) {
                tempMinRaw1 = tempMinRaw1.replace(ccode, "")
                
            	if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                    def tempMin = convertCtoF(tempMinRaw1)
                state.MinTemperature = tempMin
                }
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                state.MinTemperature = tempMinRaw1 
                 LOGINFO("Min Temperature Input = C - Output = C --No conversion required")
                }
                
            } 
            
            
 // ************************** MAX Outside TEMPERATURE *******************************************************************************                     

             LOGINFO("Checking Max Outside Temperature")
              def tempMaxRaw1 = (resp1.data.stats.sinceMidnight.maxtemptoday) 
            	if(tempMaxRaw1 ==null || tempMaxRaw1.contains("N/A")){
                state.MaxTemperature = '---'}
            
            if (tempMaxRaw1.contains("F")) {
                tempMaxRaw1 = tempMaxRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                state.MaxTemperature = tempMaxRaw1
                LOGINFO("Max Temperature Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                def tempMax = convertFtoC(tempMaxRaw1) 
                state.MaxTemperature = tempMax 
                
                }
                
            } 
            
            if (tempMaxRaw1.contains("C")) {
                tempMaxRaw1 = tempMaxRaw1.replace(ccode, "")
                
            	if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                    def tempMax = convertCtoF(tempMaxRaw1)
                state.MaxTemperature = tempMax
                }
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                state.MaxTemperature = tempMaxRaw1 
                 LOGINFO("Max Temperature Input = C - Output = C --No conversion required")
                }
                
            }            
            
// ************************** MIN Inside TEMPERATURE *******************************************************************************                     

             LOGINFO("Checking Min Inside Temperature")
              def tempMinInRaw1 = (resp1.data.stats.sinceMidnight.mininsidetemptoday) 
            	if(tempMinInRaw1 ==null || tempMinInRaw1.contains("N/A")){
                state.MinInsideTemperature = '---'}
            
            if (tempMinInRaw1.contains("F")) {
                tempMinInRaw1 = tempMinInRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                state.MinInsideTemperature = tempMinInRaw1
                LOGINFO("Min Temperature Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                def tempMinIn = convertFtoC(tempMinInRaw1) 
                state.MinInsideTemperature = tempMinIn 
                
                }
                
            } 
            
            if (tempMinInRaw1.contains("C")) {
                tempMinInRaw1 = tempMinInRaw1.replace(ccode, "")
                
            	if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                    def tempMinIn = convertCtoF(tempMinInRaw1)
                state.MinInsideTemperature = tempMinIn
                }
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                state.MinInsideTemperature = tempMinInRaw1 
                 LOGINFO("Min Temperature Input = C - Output = C --No conversion required")
                }
                
            }    
            
            
  // ************************** MAX Inside TEMPERATURE *******************************************************************************                     

             LOGINFO("Checking Max Inside Temperature")
              def tempMaxInRaw1 = (resp1.data.stats.sinceMidnight.maxinsidetemptoday) 
            	if(tempMaxInRaw1 ==null || tempMaxInRaw1.contains("N/A")){
                state.MaxInsideTemperature = '---'}
            
            if (tempMaxInRaw1.contains("F")) {
                tempMaxInRaw1 = tempMaxInRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                state.MaxInsideTemperature = tempMaxInRaw1
                LOGINFO("Max Temperature Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                def tempMaxIn = convertFtoC(tempMaxInRaw1) 
                state.MaxInsideTemperature = tempMaxIn 
                
                }
                
            } 
            
            if (tempMaxInRaw1.contains("C")) {
                tempMaxInRaw1 = tempMaxInRaw1.replace(ccode, "")
                
            	if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                    def tempMaxIn = convertCtoF(tempMaxInRaw1)
                state.MaxInsideTemperature = tempMaxIn
                }
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                state.MaxInsideTemperature = tempMaxInRaw1 
                 LOGINFO("Max Temperature Input = C - Output = C --No conversion required")
                }
                
            }             
            
            
// ************************** UV ************************************************************************************************            
            LOGINFO("Checking UV")
              def UVRaw1 = (resp1.data.stats.current.UV)
            	if(UVRaw1 ==null || UVRaw1.contains("N/A")){
                   
                	state.UV = '---'}
            	else{
                    state.UV = UVRaw1
   
                    
 // Calculate UV likelyhood of causing harm to someone *****************************************************     
          LOGINFO("Checking UV Harm")          
                    
                    LOGINFO ( "state.UV -- $state.UV")
                    if(state.UV <= '0.1'){
                        state.UVHarm = 'Zero'
                        LOGINFO  ("UV Zero -- $state.UV")
                    }
                    if(state.UV >= '0.2' && state.UV <= '2.9'){
                        state.UVHarm = 'Low'
                         LOGINFO  ( "UV Low -- $state.UV")
                    }
                    if(state.UV >= '3.0' && state.UV <= '5.9'){
                        state.UVHarm = 'Moderate'
                        LOGINFO  ( "UV Moderate -- $state.UV")
                    }
            		if(state.UV >= '6.0' && state.UV <= '7.9'){
                        state.UVHarm = 'High'
                         LOGINFO  ( "UV High -- $state.UV")
                    }
 					if(state.UV >= '8.0' && state.UV <= '9.8'){
                        state.UVHarm = 'VeryHigh'
                        LOGINFO  ( "UV VeryHigh -- $state.UV")
                    }
					if(state.UV >= "9.99"){
                        state.UVHarm = 'Extreme'
                         LOGINFO  ( "UV Extreme -- $state.UV")
                    }
                } 

// ************************** WINDCHILL ****************************************************************************************            
            LOGINFO("Checking WindChill")
              def windChillRaw1 = (resp1.data.stats.current.windchill)
            	if(windChillRaw1 ==null || windChillRaw1.contains("N/A")){
                   state.FeelsLike = '---'}
                	
            
 				if (windChillRaw1.contains("F")) {
                windChillRaw1 = windChillRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                state.FeelsLike = windChillRaw1
                LOGINFO( "FeelsLike Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                def feelslike1 = convertFtoC(windChillRaw1) 
                state.FeelsLike = feelslike1
                
                }
                
            } 
            
            if (windChillRaw1.contains("C")) {
                windChillRaw1 = windChillRaw1.replace(ccode, "")
                             
            	if(temperatureUnit == "Fahrenheit (�F)"){
            	state.TU = '&deg;F'
                def feelslike1 = convertCtoF(windChillRaw1)
                state.FeelsLike = feelslike1
                }
                if(temperatureUnit == "Celsius (�C)"){
                state.TU = '&deg;C'
                state.FeelsLike = windChillRaw1 
                 LOGINFO( "FeelsLike Input = C - Output = C --No conversion required")
                }
                
            } 
           
// ************************** WIND DIR ****************************************************************************************  
             LOGINFO("Checking Wind direction")
            def windDirRaw = (resp1.data.stats.current.windDirText)
            	if(windDirRaw != null){
					if(windDirRaw.contains("N/A")){state.WindDir = "---"}	
					else {state.WindDir = windDirRaw}
					sendEvent(name: "wind_dir", value: state.WindDir, isStateChange: true)	
                
                }                    

// ************************** PRESSURE TREND ************************************************************************************   
             LOGINFO("Checking Pressure Trend")
             def pressureTrend = (resp1.data.stats.current.barometerTrendData) 
                  if(pressureTrend != null){
					  if(pressureTrend.contains("N/A")){state.PressureTrend = "---"}
					  else if(pressureTrend.contains("-")){state.PressureTrend = "Falling"}					
					  else if(pressureTrend.contains("+")){state.PressureTrend = "Rising"}
					  else {state.PressureTrend = "Static"}
					  sendEvent(name: "pressure_trend", value:state.PressureTrend, isStateChange: true)
                  }
            
 // Basics - No units ************************************************************************************************
			state.LocalSunrise = (resp1.data.almanac.sun.sunrise)
			state.LocalSunset = (resp1.data.almanac.sun.sunset)

             sendEvent(name: "WeewxUptime", value: resp1.data.serverUptime)
             sendEvent(name: "WeewxLocation", value: resp1.data.location)
             sendEvent(name: "Refresh-Weewx", value: pollInterval)
             sendEvent(name: "localSunrise", value: state.LocalSunrise, isStateChange: true)
             sendEvent(name: "localSunset", value: state.LocalSunset, isStateChange: true)
             sendEvent(name: "uv", value: state.UV, isStateChange: true)
             sendEvent(name: "uvHarm", value: state.UVHarm, isStateChange: true)
             sendEvent(name: "LastUpdate-Weewx", value: resp1.data.time)
            
// // Send Events  - WITH UNITS ********************************************************************************************            
              if(state.DisplayUnits == true){  
                         
                  sendEvent(name: "illuminance", value: state.Illuminance +" " +state.IU)
                  sendEvent(name: "solarradiation", value: state.SolarRadiation +" " +state.SRU)
                  sendEvent(name: "dewpoint", value: state.Dewpoint +" " +state.TU)
                  sendEvent(name: "humidity", value: state.Humidity +" " +state.HU)
                  sendEvent(name: "pressure", value: state.Pressure +" " +state.PU) 
                  sendEvent(name: "wind", value: state.WindSpeed +" " +state.SU)
                  sendEvent(name: "wind_gust", value: state.WindGust +" " +state.SU)
                  sendEvent(name: "maxwindGust", value: state.maxwindGust +" " +state.SU)
                  sendEvent(name: "inside_temperature", value: state.InsideTemp +" " +state.TU)
                  sendEvent(name: "inside_humidity", value: state.InsideHumidity +" " +state.HU)  
                  sendEvent(name: "temperature", value: state.Temperature +" " +state.TU)
                  sendEvent(name: "rain_rate", value: state.Rainrate +" " +state.RRU)
                  sendEvent(name: "precip_today", value: state.RainToday +" " +state.RU) 
                  sendEvent(name: "precip_1hr", value: state.Rainrate +" " +state.RU)
                  sendEvent(name: "feelsLike", value: state.FeelsLike +" " +state.TU)                
                  sendEvent(name: "tempMaxTemperature", value: state.MaxTemperature +" " +state.TU)
    			  sendEvent(name: "tempMinToday", value: state.MinTemperature +" " +state.TU)
                  sendEvent(name: "tempMaxInsideToday", value: state.MaxInsideTemperature +" " +state.TU)
    			  sendEvent(name: "tempMinInsideToday", value: state.MinInsideTemperature +" " +state.TU)
              }
            
// // Send Events  - WITHOUT UNITS ****************************************************************************************
            
            if(state.DisplayUnits == false){
                
                  sendEvent(name: "illuminance", value: state.Illuminance, unit: "lux", isStateChange: true)    
                  sendEvent(name: "solarradiation", value: state.SolarRadiation, unit: "lux", isStateChange: true)
                  sendEvent(name: "dewpoint", value: state.Dewpoint, isStateChange: true)
                  sendEvent(name: "humidity", value: state.Humidity, isStateChange: true)
                  sendEvent(name: "pressure", value: state.Pressure, isStateChange: true)
                  sendEvent(name: "wind", value: state.WindSpeed, isStateChange: true)
                  sendEvent(name: "wind_gust", value: state.WindGust, isStateChange: true)
                  sendEvent(name: "maxwindGust", value: state.maxwindGust, isStateChange: true)
                  sendEvent(name: "inside_temperature", value: state.InsideTemp, isStateChange: true)
                  sendEvent(name: "inside_humidity", value: state.InsideHumidity, isStateChange: true)   
                  sendEvent(name: "temperature", value: state.Temperature, isStateChange: true)
                  sendEvent(name: "rain_rate", value: state.Rainrate, isStateChange: true)  
                  sendEvent(name: "precip_today", value: state.RainToday, isStateChange: true)  
                  sendEvent(name: "precip_1hr", value: state.Rainrate, isStateChange: true)  
                  sendEvent(name: "feelsLike", value: state.FeelsLike, isStateChange: true) 
             	  sendEvent(name: "tempMaxToday", value: state.MaxTemperature, isStateChange: true)
    			  sendEvent(name: "tempMinToday", value: state.MinTemperature, isStateChange: true)
                  sendEvent(name: "tempMaxInsideToday", value: state.MaxInsideTemperature, isStateChange: true)
    			  sendEvent(name: "tempMinInsideToday", value: state.MinInsideTemperature, isStateChange: true)
        }
   } 
        
    } catch (e) {
        log.error "something went wrong: $e"
    }
}

def checkInput(){
    listInput = [
		"Blank Line",
		"Free Text",
        "City",
		"State - County",
        "External Temp",
        "External Temp (Feels Like)",
        "External Temp (Feels Like) Humidity",
		"Forecast Low, Forecast High",
		"Temp Feels Like",
		"Humidity",
        "Wind Speed",
		"Wind Speed, Wind Gust",
		"Wind Speed, Wind Direction, Wind Gust",
		"Wind Speed, Wind Direction",
		"Weather Current",
        "Weather Forecast",
		"Chance of Rain",
        "Rain Today",
        "Sunrise & Sunset"
]
     return listInput
}

 // Future feature
def fSize1(){
	if(fsize == '1') {state.fsize = '-2'}
	if(fsize == '2') {state.fsize = '-1'}
	if(fsize == '3') {state.fsize = '+1'}
	if(fsize == '4') {state.fsize = '+3'}
	if(fsize == '5') {state.fsize = '+5'}	
}

def fontSize(){
	listInput1 = ["1","2","3","4","5"]  
	return listInput1
}

def sData(){
	state.slot1 = slot1
	state.slot2 = slot2
	state.slot3 = slot3
	state.slot4 = slot4
	state.slot5 = slot5
	state.slot6 = slot6
	state.slot7 = slot7
	state.slot8 = slot8
	state.slot1Text = slot1Text1
	state.slot2Text = slot2Text1
	state.slot3Text = slot3Text1
	state.slot4Text = slot4Text1
	state.slot5Text = slot5Text1
	state.slot6Text = slot6Text1
	state.slot7Text = slot7Text1
	state.slot8Text = slot8Text1	
	if(slot1){
	if(state.slot1 == "Blank Line"){state.slot1Data = "  "}
	if(state.slot1 == "Free Text"){state.slot1Data = state.slot1Text}
	if(state.slot1 == "City"){state.slot1Data = state.city}
	if(state.slot1 == "State - County"){state.slot1Data =state.county}
	if(state.slot1 == "External Temp"){state.slot1Data = "Temp: " +state.Temperature}
	if(state.slot1 == "External Temp (Feels Like)"){state.slot1Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +")"}
	if(state.slot1 == "External Temp (Feels Like) Humidity"){state.slot1Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +") Hum:" +state.Humidity +"%"}
	if(state.slot1 == "Forecast Low, Forecast High"){state.slot1Data = "High: " +state.ForecastHigh + state.TU +", Low: " +state.ForecastLow+ state.TU}
	if(state.slot1 == "Temp Feels Like"){state.slot1Data = "Temp Feels Like: " +state.FeelsLike + state.TU}
	if(state.slot1 == "Humidity"){state.slot1Data = "Humidity: " +state.Humidity +"%"}
	if(state.slot1 == "Wind Speed"){state.slot1Data = "Wind: " +state.WindSpeed +state.SU}
	if(state.slot1 == "Wind Speed, Wind Gust"){state.slot1Data = "Wind: " +state.WindSpeed +state.SU +", Gust: " +state.WindGust +state.SU}
	if(state.slot1 == "Wind Speed, Wind Direction, Wind Gust"){state.slot1Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir + ", Gust: " +state.WindGust +state.SU}
	if(state.slot1 == "Wind Speed, Wind Direction"){state.slot1Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir}
	if(state.slot1 == "Weather Forecast"){state.slot1Data = "Forecast Weather: " +state.weatherForecast}
	if(state.slot1 == "Weather Current"){state.slot1Data = "Current Weather: " +state.weather}
	if(state.slot1 == "Chance of Rain"){state.slot1Data = "Chance of Rain: " +state.chanceOfRain +"%"}
	if(state.slot1 == "Rain Today"){state.slot1Data = "Rain Today: " +state.RainToday +state.RU}
	if(state.slot1 == "Sunrise & Sunset"){state.slot1Data = "Sunrise: " +state.LocalSunrise +", Sunset: " +state.LocalSunset}
	}	
	if(slot2){
	if(state.slot2 == "Blank Line"){state.slot2Data = "  "}
	if(state.slot2 == "Free Text"){state.slot2Data = state.slot2Text}
	if(state.slot2 == "City"){state.slot2Data = state.city}
	if(state.slot2 == "State - County"){state.slot2Data =state.county}
	if(state.slot2 == "External Temp"){state.slot2Data = "Temp: " +state.Temperature}
	if(state.slot2 == "External Temp (Feels Like)"){state.slot2Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +")"}
	if(state.slot2 == "External Temp (Feels Like) Humidity"){state.slot2Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +") Hum:" +state.Humidity +"%"}
	if(state.slot2 == "Forecast Low, Forecast High"){state.slot2Data = "High: " +state.ForecastHigh + state.TU +", Low: " +state.ForecastLow+ state.TU}
	if(state.slot2 == "Temp Feels Like"){state.slot2Data = "Temp Feels Like: " +state.FeelsLike + state.TU}
	if(state.slot2 == "Humidity"){state.slot2Data = "Humidity: " +state.Humidity +"%"}
	if(state.slot2 == "Wind Speed"){state.slot2Data = "Wind: " +state.WindSpeed +state.SU}
	if(state.slot2 == "Wind Speed, Wind Gust"){state.slot2Data = "Wind: " +state.WindSpeed +state.SU +", Gust: " +state.WindGust +state.SU}
	if(state.slot2 == "Wind Speed, Wind Direction, Wind Gust"){state.slot2Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir + ", Gust: " +state.WindGust +state.SU}
	if(state.slot2 == "Wind Speed, Wind Direction"){state.slot2Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir}
	if(state.slot2 == "Weather Forecast"){state.slot2Data = "Forecast Weather: " +state.weatherForecast}
	if(state.slot2 == "Weather Current"){state.slot2Data = "Current Weather: " +state.weather}
	if(state.slot2 == "Chance of Rain"){state.slot2Data = "Chance of Rain: " +state.chanceOfRain +"%"}
	if(state.slot2 == "Rain Today"){state.slot2Data = "Rain Today: " +state.RainToday +state.RU}
	if(state.slot2 == "Sunrise & Sunset"){state.slot2Data = "Sunrise: " +state.LocalSunrise +", Sunset: " +state.LocalSunset}
	}	
	if(slot3){
	if(state.slot3 == "Blank Line"){state.slot3Data = "  "}
	if(state.slot3 == "Free Text"){state.slot3Data = state.slot3Text}
	if(state.slot3 == "City"){state.slot3Data = state.city}
	if(state.slot3 == "State - County"){state.slot3Data =state.county}
	if(state.slot3 == "External Temp"){state.slot3Data = "Temp: " +state.Temperature}
	if(state.slot3 == "External Temp (Feels Like)"){state.slot3Data = "Temp: " +state.Temperature+ " (Feels Like: " +state.FeelsLike +")"}
	if(state.slot3 == "External Temp (Feels Like) Humidity"){state.slot3Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +") Hum:" +state.Humidity +"%"}
	if(state.slot3 == "Forecast Low, Forecast High"){state.slot3Data = "High: " +state.ForecastHigh + state.TU +", Low: " +state.ForecastLow+ state.TU}
	if(state.slot3 == "Temp Feels Like"){state.slot3Data = "Temp Feels Like: " +state.FeelsLike + state.TU}
	if(state.slot3 == "Humidity"){state.slot3Data = "Humidity: " +state.Humidity +"%"}
	if(state.slot3 == "Wind Speed"){state.slot3Data = "Wind: " +state.WindSpeed +state.SU}
	if(state.slot3 == "Wind Speed, Wind Gust"){state.slot3Data = "Wind: " +state.WindSpeed +state.SU +", Gust: " +state.WindGust +state.SU}
	if(state.slot3 == "Wind Speed, Wind Direction, Wind Gust"){state.slot3Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir + ", Gust: " +state.WindGust +state.SU}
	if(state.slot3 == "Wind Speed, Wind Direction"){state.slot3Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir}
	if(state.slot3 == "Weather Forecast"){state.slot3Data = "Forecast Weather: " +state.weatherForecast}
	if(state.slot3 == "Weather Current"){state.slot3Data = "Current Weather: " +state.weather}
	if(state.slot3 == "Chance of Rain"){state.slot3Data = "Chance of Rain: " +state.chanceOfRain +"%"}
	if(state.slot3 == "Rain Today"){state.slot3Data = "Rain Today: " +state.RainToday +state.RU}
	if(state.slot3 == "Sunrise & Sunset"){state.slot3Data = "Sunrise: " +state.LocalSunrise +", Sunset: " +state.LocalSunset}
	}
	if(slot4){
	if(state.slot4 == "Blank Line"){state.slot4Data = "  "}
	if(state.slot4 == "Free Text"){state.slot4Data = state.slot4Text}
	if(state.slot4 == "City"){state.slot4Data = state.city}
	if(state.slot4 == "State - County"){state.slot4Data =state.county}
	if(state.slot4 == "External Temp"){state.slot4Data = "Temp: " +state.Temperature}
	if(state.slot4 == "External Temp (Feels Like)"){state.slot4Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +")"}
	if(state.slot4 == "External Temp (Feels Like) Humidity"){state.slot4Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +") Hum:" +state.Humidity +"%"}
	if(state.slot4 == "Forecast Low, Forecast High"){state.slot4Data = "High: " +state.ForecastHigh + state.TU +", Low: " +state.ForecastLow+ state.TU}
	if(state.slot4 == "Temp Feels Like"){state.slot4Data = "Temp Feels Like: " +state.FeelsLike + state.TU}
	if(state.slot4 == "Humidity"){state.slot4Data = "Humidity: " +state.Humidity +"%"}
	if(state.slot4 == "Wind Speed"){state.slot4Data = "Wind: " +state.WindSpeed +state.SU}
	if(state.slot4 == "Wind Speed, Wind Gust"){state.slot4Data = "Wind: " +state.WindSpeed +state.SU +", Gust: " +state.WindGust +state.SU}
	if(state.slot4 == "Wind Speed, Wind Direction, Wind Gust"){state.slot4Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir + ", Gust: " +state.WindGust +state.SU}
	if(state.slot4 == "Wind Speed, Wind Direction"){state.slot4Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir}
	if(state.slot4 == "Weather Forecast"){state.slot4Data = "Forecast Weather: " +state.weatherForecast}
	if(state.slot4 == "Weather Current"){state.slot4Data = "Current Weather: " +state.weather}
	if(state.slot4 == "Chance of Rain"){state.slot4Data = "Chance of Rain: " +state.chanceOfRain +"%"}
	if(state.slot4 == "Rain Today"){state.slot4Data = "Rain Today: " +state.RainToday +state.RU}
	if(state.slot4 == "Sunrise & Sunset"){state.slot4Data = "Sunrise: " +state.LocalSunrise +", Sunset: " +state.LocalSunset}
	}
	if(slot5){
	if(state.slot5 == "Blank Line"){state.slot5Data = "  "}
	if(state.slot5 == "Free Text"){state.slot5Data = state.slot5Text}
	if(state.slot5 == "City"){state.slot5Data = state.city}
	if(state.slot5 == "State - County"){state.slot5Data =state.county}
	if(state.slot5 == "External Temp"){state.slot5Data = "Temp: " +state.Temperature}
	if(state.slot5 == "External Temp (Feels Like)"){state.slot5Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +")"}
	if(state.slot5 == "External Temp (Feels Like) Humidity"){state.slot5Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +") Hum:" +state.Humidity +"%"}
	if(state.slot5 == "Forecast Low, Forecast High"){state.slot5Data = "High: " +state.ForecastHigh + state.TU +", Low: " +state.ForecastLow+ state.TU}
	if(state.slot5 == "Temp Feels Like"){state.slot5Data = "Temp Feels Like: " +state.FeelsLike + state.TU}
	if(state.slot5 == "Humidity"){state.slot5Data = "Humidity: " +state.Humidity +"%"}
	if(state.slot5 == "Wind Speed"){state.slot5Data = "Wind: " +state.WindSpeed +state.SU}
	if(state.slot5 == "Wind Speed, Wind Gust"){state.slot5Data = "Wind: " +state.WindSpeed +state.SU +", Gust: " +state.WindGust +state.SU}
	if(state.slot5 == "Wind Speed, Wind Direction, Wind Gust"){state.slot5Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir + ", Gust: " +state.WindGust +state.SU}
	if(state.slot5 == "Wind Speed, Wind Direction"){state.slot5Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir}
	if(state.slot5 == "Weather Forecast"){state.slot5Data = "Forecast Weather: " +state.weatherForecast}
	if(state.slot5 == "Weather Current"){state.slot5Data = "Current Weather: " +state.weather}
	if(state.slot5 == "Chance of Rain"){state.slot5Data = "Chance of Rain: " +state.chanceOfRain +"%"}
	if(state.slot5 == "Rain Today"){state.slot5Data = "Rain Today: " +state.RainToday +state.RU}
	if(state.slot5 == "Sunrise & Sunset"){state.slot5Data = "Sunrise: " +state.LocalSunrise +", Sunset: " +state.LocalSunset}
	}
	if(slot6){
	if(state.slot6 == "Blank Line"){state.slot6Data = "  "}
	if(state.slot6 == "Free Text"){state.slot6Data = state.slot6Text}
	if(state.slot6 == "City"){state.slot6Data = state.city}
	if(state.slot6 == "State - County"){state.slot6Data =state.county}
	if(state.slot6 == "External Temp"){state.slot6Data = "Temp: " +state.Temperature}
	if(state.slot6 == "External Temp (Feels Like)"){state.slot6Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +")"}
	if(state.slot6 == "External Temp (Feels Like) Humidity"){state.slot6Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +") Hum:" +state.Humidity +"%"}
	if(state.slot6 == "Forecast Low, Forecast High"){state.slot6Data = "High: " +state.ForecastHigh + state.TU +", Low: " +state.ForecastLow+ state.TU}
	if(state.slot6 == "Temp Feels Like"){state.slot6Data = "Temp Feels Like: " +state.FeelsLike + state.TU}
	if(state.slot6 == "Humidity"){state.slot6Data = "Humidity: " +state.Humidity +"%"}
	if(state.slot6 == "Wind Speed"){state.slot6Data = "Wind: " +state.WindSpeed +state.SU}
	if(state.slot6 == "Wind Speed, Wind Gust"){state.slot6Data = "Wind: " +state.WindSpeed +state.SU +", Gust: " +state.WindGust +state.SU}
	if(state.slot6 == "Wind Speed, Wind Direction, Wind Gust"){state.slot6Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir + ", Gust: " +state.WindGust +state.SU}
	if(state.slot6 == "Wind Speed, Wind Direction"){state.slot6Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir}
	if(state.slot6 == "Weather Forecast"){state.slot6Data = "Forecast Weather: " +state.weatherForecast}
	if(state.slot6 == "Weather Current"){state.slot6Data = "Current Weather: " +state.weather}
	if(state.slot6 == "Chance of Rain"){state.slot6Data = "Chance of Rain: " +state.chanceOfRain +"%"}
	if(state.slot6 == "Rain Today"){state.slot6Data = "Rain Today: " +state.RainToday +state.RU}
	if(state.slot6 == "Sunrise & Sunset"){state.slot6Data = "Sunrise: " +state.LocalSunrise +", Sunset: " +state.LocalSunset}
	}
	if(slot7){
	if(state.slot7 == "Blank Line"){state.slot7Data = "  "}
	if(state.slot7 == "Free Text"){state.slot7Data = state.slot7Text}
	if(state.slot7 == "City"){state.slot7Data = state.city}
	if(state.slot7 == "State - County"){state.slot7Data =state.county}
    if(state.slot7 == "External Temp"){state.slot7Data = "Temp: " +state.Temperature}
	if(state.slot7 == "External Temp (Feels Like)"){state.slot7Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +")"}
	if(state.slot7 == "External Temp (Feels Like) Humidity"){state.slot7Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +") Hum:" +state.Humidity +"%"}
	if(state.slot7 == "Forecast Low, Forecast High"){state.slot7Data = "High: " +state.ForecastHigh + state.TU +", Low: " +state.ForecastLow+ state.TU}
	if(state.slot7 == "Temp Feels Like"){state.slot7Data = "Temp Feels Like: " +state.FeelsLike + state.TU}
	if(state.slot7 == "Humidity"){state.slot7Data = "Humidity: " +state.Humidity +"%"}
	if(state.slot7 == "Wind Speed"){state.slot7Data = "Wind: " +state.WindSpeed +state.SU}
	if(state.slot7 == "Wind Speed, Wind Gust"){state.slot7Data = "Wind: " +state.WindSpeed +state.SU +", Gust: " +state.WindGust +state.SU}
	if(state.slot7 == "Wind Speed, Wind Direction, Wind Gust"){state.slot7Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir + ", Gust: " +state.WindGust +state.SU}
	if(state.slot7 == "Wind Speed, Wind Direction"){state.slot7Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir}
	if(state.slot7 == "Weather Forecast"){state.slot7Data = "Forecast Weather: " +state.weatherForecast}
	if(state.slot7 == "Weather Current"){state.slot7Data = "Current Weather: " +state.weather}
	if(state.slot7 == "Chance of Rain"){state.slot7Data = "Chance of Rain: " +state.chanceOfRain +"%"}
	if(state.slot7 == "Rain Today"){state.slot7Data = "Rain Today: " +state.RainToday +state.RU}
	if(state.slot7 == "Sunrise & Sunset"){state.slot7Data = "Sunrise: " +state.LocalSunrise +", Sunset: " +state.LocalSunset}
	}
	if(slot8){
	if(state.slot8 == "Blank Line"){state.slot8Data = "  "}
	if(state.slot8 == "Free Text"){state.slot8Data = state.slot8Text}
	if(state.slot8 == "City"){state.slot8Data = state.city}
	if(state.slot8 == "State - County"){state.slot8Data =state.county}
	if(state.slot8 == "External Temp"){state.slot8Data = "Temp: " +state.Temperature}
	if(state.slot8 == "External Temp (Feels Like)"){state.slot8Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +")"}
	if(state.slot8 == "External Temp (Feels Like) Humidity"){state.slot8Data = "Temp: " +state.Temperature + " (Feels Like: " +state.FeelsLike +") Hum:" +state.Humidity +"%"}
	if(state.slot8 == "Forecast Low, Forecast High"){state.slot8Data = "High: " +state.ForecastHigh + state.TU +", Low: " +state.ForecastLow+ state.TU}
	if(state.slot8 == "Temp Feels Like"){state.slot8Data = "Temp Feels Like: " +state.FeelsLike + state.TU}
	if(state.slot8 == "Humidity"){state.slot8Data = "Humidity: " +state.Humidity +"%"}
	if(state.slot8 == "Wind Speed"){state.slot8Data = "Wind: " +state.WindSpeed +state.SU}
	if(state.slot8 == "Wind Speed, Wind Gust"){state.slot8Data = "Wind: " +state.WindSpeed +state.SU +", Gust: " +state.WindGust +state.SU}
	if(state.slot8 == "Wind Speed, Wind Direction, Wind Gust"){state.slot8Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir + ", Gust: " +state.WindGust +state.SU}
	if(state.slot8 == "Wind Speed, Wind Direction"){state.slot8Data = "Wind: " +state.WindSpeed +state.SU +", Dir: " +state.WindDir}
	if(state.slot8 == "Weather Forecast"){state.slot8Data = "Forecast Weather: " +state.weatherForecast}
	if(state.slot8 == "Weather Current"){state.slot8Data = "Current Weather: " +state.weather}
	if(state.slot8 == "Chance of Rain"){state.slot8Data = "Chance of Rain: " +state.chanceOfRain +"%"}
	if(state.slot8 == "Rain Today"){state.slot8Data = "Rain Today: " +state.RainToday +state.RU}
	if(state.slot8 == "Sunrise & Sunset"){state.slot8Data = "Sunrise: " + state.LocalSunrise +", Sunset: " +state.LocalSunset}
	}
}
	
def setup(){
	dFontW()
	dfontC()
	fSize1()
//	tempColour()
}
	
// Future update
def tempColour(){
	if(tempColour1 == true){
		state.temperature = state.Temperature.toDouble()
		state.feelslike = state.FeelsLike.toDouble()
	if(temperatureUnit == "Fahrenheit (�F)"){	
		
	}	
	if(temperatureUnit == "Celsius (�C)"){
		
		if(state.temperature < 0){state.fc = 046AF4}
		if(state.feelslike < 0){state.fc = 046AF4}
	}
  }		
}

def standardDash(){
	setup()
	def dashFormat = ""
	dashFormat +="<font color = $state.fc>$state.fw1 ${state.city} Weather $state.fw2</font><br>"
	dashFormat +="<font color = $state.fc>$state.fw1 Current Weather: ${state.weather} $state.fw2</font><br>"
	dashFormat +="<font color = $state.fc>$state.fw1 Forecast Weather: ${state.weatherForecast} $state.fw2</font><br>"
	dashFormat +="<font color = $state.fc>$state.fw1 Temp: ${state.Temperature} ${state.TU} - Feels like ${state.FeelsLike} ${state.TU}$state.fw2</font><br>"
	dashFormat +="<font color = $state.fc>$state.fw1 Wind: ${state.WindSpeed}${state.SU}, Gust: ${state.WindGust}${state.SU}$state.fw2</font><br>"
	dashFormat +="<font color = $state.fc>$state.fw1 Pressure: ${state.Pressure}${state.PU}$state.fw2</font><br>"
	dashFormat +="<font color = $state.fc>$state.fw1 Humidity: ${state.Humidity}% $state.fw2</font><br>"
	dashFormat +="<font color = $state.fc>$state.fw1 Chance of Rain: ${state.chanceOfRain}% $state.fw2</font><br>"
	sendEvent(name: "WeatherDisplay", value: dashFormat) // , isStateChange: true)
}

def	advancedDash(){
	setup()
	sData()
	 def dashFormat = ""
	// size = $state.fsize
	if(state.slot1Data != null){dashFormat +="<font color = $state.fc>$state.fw1 ${state.slot1Data} $state.fw2</font><br>"}
	if(state.slot2Data != null){dashFormat +="<font color = $state.fc>$state.fw1 ${state.slot2Data} $state.fw2</font><br>"}
	if(state.slot3Data != null){dashFormat +="<font color = $state.fc>$state.fw1 ${state.slot3Data} $state.fw2</font><br>"}
	if(state.slot4Data != null){dashFormat +="<font color = $state.fc>$state.fw1 ${state.slot4Data} $state.fw2</font><br>"}
	if(state.slot5Data != null){dashFormat +="<font color = $state.fc>$state.fw1 ${state.slot5Data} $state.fw2</font><br>"}
	if(state.slot6Data != null){dashFormat +="<font color = $state.fc>$state.fw1 ${state.slot6Data} $state.fw2</font><BR>"}
	if(state.slot7Data != null){dashFormat +="<font color = $state.fc>$state.fw1 ${state.slot7Data} $state.fw2</font><BR>"}
	if(state.slot8Data != null){dashFormat +="<font color = $state.fc>$state.fw1 ${state.slot8Data} $state.fw2</font><BR>"}
	sendEvent(name: "WeatherDisplay", value: dashFormat) // , isStateChange: true)
}

def PollInside(){
    LOGINFO( "Polling internal temperature and humidity and sending it as 'standard' temperature & humidity data" )   
    state.Temperature = state.InsideTemp
    state.Humidity = state.InsideHumidity
    
    if(state.DisplayUnits == true){ 
		sendEvent(name: "humidity", value: state.Humidity +" " +state.HU)
		sendEvent(name: "temperature", value: state.Temperature +" " +state.TU)
    }
   
    if(state.DisplayUnits == false){ 
		sendEvent(name: "humidity", value: state.Humidity)
		sendEvent(name: "temperature", value: state.Temperature)
    }
}

def getFcode(){
     def charF1 ="&-#-1-7-6-;-F"
     def charF = charF1.replace("-", "")
	 return charF
}

def getCcode(){
   def charC1 ="&-#-1-7-6-;-C"
    def charC = charC1.replace("-", "")
	return charC
}

def dfontC(){
    state.fc = "${fcolour}"
}

def getWmcode(){
    def wm1 ="W-/-m-&-#-1-7-8;"
    def wm = wm1.replace("-", "")
	return wm
}

def convertFtoC(temperatureIn){
    LOGDEBUG( "Converting F to C")
    def tempIn = temperatureIn.toFloat()
    LOGDEBUG("tempIn = $tempIn")
    def tempCalc = ((tempIn - 32) *0.5556)  
    def tempOut1 = tempCalc.round(state.DecimalPlaces)
    def tempOut = tempOut1
    LOGDEBUG( "tempOut =  $tempOut")
	return tempOut
}
            
def dFontW(){
	if(fweight == "Normal"){
	state.fw1 = ""
	state.fw2 = ""}
	if(fweight == "Bold"){
	state.fw1 = "<b><strong>"
	state.fw2 = "</b></strong>"}
	if(fweight == "Italic"){
	state.fw1 = "<i>"
	state.fw2 = "</i>"}
}

def convertCtoF(temperatureIn){
    LOGDEBUG( "Converting C to F")
    def tempIn = temperatureIn.toFloat()
    LOGDEBUG( "tempIn = $tempIn")
    def tempCalc = ((tempIn * 1.8) + 32)  
    def tempOut1 = tempCalc.round(state.DecimalPlaces)
    def tempOut = tempOut1
    LOGDEBUG( "tempOut =  $tempOut")
	return tempOut
}   
 
def convertINtoMM(unitIn){
      LOGDEBUG( "Converting IN to MM" )           
      def tempIn1 = unitIn.toFloat()           
     LOGDEBUG( "tempIn1 = $tempIn1" )
    def tempCalc1 = (tempIn1 * 25.4)
    def tempOut2 = tempCalc1.round(state.DecimalPlaces)
    def tempOut1 = tempOut2
    LOGDEBUG( "tempOut1 =  $tempOut1")
	return tempOut1              
}

def convertMMtoIN(unitIn){
      LOGDEBUG( "Converting IN to MM" )            
      def tempIn1 = unitIn.toFloat()           
      LOGDEBUG( "tempIn1 = $tempIn1")
    def tempCalc1 = (tempIn1/25.4)
    def tempOut2 = tempCalc1.round(state.DecimalPlaces)
    def tempOut1 = tempOut2
    LOGDEBUG( "tempOut1 =  $tempOut1")
	return tempOut1              
}               
               
def convertMBtoIN(pressureIn){
      LOGDEBUG( "Converting MBAR to INHg")           
     def pressIn1 = pressureIn.toFloat()           
      LOGDEBUG("Pressure In = $pressIn1") 
    def pressCalc1 = (pressIn1 * 0.02953)
    def pressOut2 = pressCalc1.round(state.DecimalPlaces)
    def pressOut1 = pressOut2
    LOGDEBUG( "Pressure Out =  $pressOut1")
	return pressOut1              
}                              
               
def convertINtoMB(pressureIn){
      LOGDEBUG( "Converting INHg to MBAR" )            
      def pressIn1 = pressureIn.toFloat()           
      LOGDEBUG( "Pressure In = $pressIn1" )
    def pressCalc1 = (pressIn1 * 33.8638815)
    def pressOut2 = pressCalc1.round(state.DecimalPlaces)
    def pressOut1 = pressOut2
   LOGDEBUG( "Pressure Out =  $pressOut1")
	return pressOut1              
}
               
def convertMPHtoKPH(speed1In) {
  LOGDEBUG( "Converting MPH to KPH")            
      def speed1 = speed1In.toFloat()           
     LOGDEBUG( "Speed In = $speed1In")
    def speedCalc1 = (speed1In * 1.60934)
    def speedOut2 = speedCalc1.round(state.DecimalPlaces)
    def speedOut1 = speedOut2
    LOGDEBUG("Speed Out =  $pressOut1")
	return speedOut1              
}

def convertKPHtoMPH(speed1In) {
  LOGDEBUG("Converting KPH to MPH" )            
      def speed1 = speed1In.toFloat()           
  LOGDEBUG( "Speed In = $speed1In") 
    def speedCalc1 = (speed1In * 0.621371)
    def speedOut2 = speedCalc1.round(state.DecimalPlaces)
    def speedOut1 = speedOut2
  LOGDEBUG( "Speed Out =  $pressOut1")
	return speedOut1              
}

def convertKelvinToF(tempIn){
     LOGDEBUG("Converting Kelvin to F" )  
 def tempK = tempIn.toFloat()
     LOGDEBUG("Kelvin in = $tempK")
    def tempFahrenheitCalc = ((tempK * 9/5) - 459.67)
    def tempFahrenheit = tempFahrenheitCalc.round(state.DecimalPlaces)
     LOGDEBUG( "F out =  $tempFahrenheit")
	return tempFahrenheit
}

def convertKelvinToC(tempIn){
       LOGDEBUG("Converting Kelvin to C" )  

def tempK = tempIn.toFloat()
     LOGDEBUG("Kelvin in = $tempK")

def tempCelsiusCalc = (tempK - 273.15)

def tempCelsius = tempCelsiusCalc.round(state.DecimalPlaces)
     LOGDEBUG( "C out =  $tempCelsius")
	return tempCelsius 
}

// define debug action ***********************************
def logCheck(){
state.checkLog = logSet
    if(state.checkLog == true){
    log.info "All Logging Enabled"
    }
    else if(state.checkLog == false){
    log.info "Further Logging Disabled"
    }
}

def LOGDEBUG(txt){
    try {
    	if(state.checkLog == true){ log.debug("Weewx Driver - DEBUG:  ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}

def LOGINFO(txt){
    try {
    	if(state.checkLog == true){log.info("Weewx Driver - INFO:  ${txt}") }
    } catch(ex) {
    	log.error("LOGINFO unable to output requested data!")
    }
}
