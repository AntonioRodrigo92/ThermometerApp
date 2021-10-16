<h1>ThermometerApp</h1>
<br>
<p>
<strong>Description</strong> - This app has the main goal of measuring, handle and store data regarding temperature and humidity, using a <u>Xiaomi Mijia Thermometer 2</u>.
</p>
<br>
<p>
<strong>How to use:</strong>
<ul>
<li>modify the user_input.config file to your needs</li>
<li>run the jar: java -jar <file.jar> <path-to-user_input.config/user_input.config>
</ul>
</p>
<br>
<p>
<strong>How does it work?</strong> 
<ul>
<li>The app regularly requests bluetooth readings (i.e., every 20 seconds).</li>
<li>The readings are handled and analysed, and every 5 minutes, a reading with a mean value is inserted in a MongoCollection.</li>
<li>If several readings are outside the limits defined by the user, an e-mail is sent, warning the user.</li>
<li><strong>The user may change the user_input.config while the app is running, allowing a live refresh of the apps behaviour, without restarting it.</strong></li>
</ul>
</p>
