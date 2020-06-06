<?php
$servername = "localhost";
$username = "username";
$password = "password";
$dbname = "SUMO";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// sql to create table
$sql = "CREATE TABLE MetaData (
id VARCHAR (30) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
description VARCHAR NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table MyGuests created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE state (
md_key VARCHAR (30) REFERENCES MetaData(id),
data VARCHAR NOT NULL,
timestep FLOAT NOT NULL,
state_id VARCHAR NOT NULL PRIMARY KEY ,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table MetaData created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE configuration (
md_key VARCHAR (30) REFERENCES MetaData(id),
id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
begin FLOAT NOT NULL,
finish FLOAT NOT NULL,
step FLOAT NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table configuration created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE routes_xml (
md_key VARCHAR (30) REFERENCES MetaData(id),
id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table routes_xml created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE net (
id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
conf_key INT UNSIGNED AUTO_INCREMENT REFERENCES configuration(id),
version VARCHAR (30) NOT NULL,
junction_corner_detail INT UNSIGNED AUTO_INCREMENT NOT NULL,
limit_turn_speed INT UNSIGNED AUTO_INCREMENT NOT NULL,
location VARCHAR (30) NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table net created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE vehicle_type (
id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
type VARCHAR (20) NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table vehicle_type created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE vehicle (
id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
type_id INT UNSIGNED AUTO_INCREMENT REFERENCES vehicle_type(id),
routes_xml_id INT UNSIGNED AUTO_INCREMENT REFERENCES routes_xml(id),
depart INT NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table vehicle created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE vehicle_state (
id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
vehicle_id INT UNSIGNED AUTO_INCREMENT REFERENCES vehicle(id),
state_id VARCHAR UNSIGNED AUTO_INCREMENT REFERENCES state(state_id),
speed_factor FLOAT NOT NULL, 
state VARCHAR (20) NOT NULL,
pos FLOAT NOT NULL,
speed FLOAT NOT NULL,
postlat FLOAT NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table vehicle_state created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE routes (
id VARCHAR UNSIGNED AUTO_INCREMENT PRIMARY KEY,
routes_xml_id INT UNSIGNED AUTO_INCREMENT REFERENCES routes_xml(id),
edges VARCHAR NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table routes created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE route_state (
state_id VARCHAR UNSIGNED AUTO_INCREMENT REFERENCES state(id),
routes_id VARCHAR NOT NULL REFERENCES routes(id),
)";

if ($conn->query($sql) === TRUE) {
    echo "Table route_state created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE edge (
id VARCHAR NOT NULL PRIMARY KEY,
lane_id VARCHAR NOT NULL REFERENCES lane(id),
num_lanes INT NOT NULL,
from_junction VARCHAR NOT NULL REFERENCES juction(id),
to_junction VARCHAR NOT NULL REFERENCES juction(id),
)";

if ($conn->query($sql) === TRUE) {
    echo "Table edge created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE lane (
id VARCHAR NOT NULL PRIMARY KEY ,
edge_id VARCHAR NOT NULL REFERENCES edge(id),
index INT NOT NULL,
speed FLOAT,
lenght FLOAT NOT NULL,
shape VARCHAR NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table lane created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE lane_state (
state_id VARCHAR UNSIGNED AUTO_INCREMENT REFERENCES state(state_id),
lane_id VARCHAR NOT NULL REFERENCES lane(id),
vehicle_id INT UNSIGNED AUTO_INCREMENT REFERENCES vehicle(id),
)";

if ($conn->query($sql) === TRUE) {
    echo "Table lane_state created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE junction (
id VARCHAR NOT NULL PRIMARY KEY,
type VARCHAR NOT NULL,
x FLOAT,
y FLOAT,
include_lane VARCHAR NOT NULL REFERENCES lane(id),
int_lanes VARCHAR NOT NULL,
shape VARCHAR NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table junction created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$sql = "CREATE TABLE connections (
from_edge VARCHAR NOT NULL REFERENCES edge(id),
to_edge VARCHAR NOT NULL REFERENCES edge(id),
from_lane VARCHAR NOT NULL REFERENCES lane(id),
to_lane VARCHAR NOT NULL REFERENCES lane(id),
via_junction VARCHAR NOT NULL REFERENCES junction(id),
dir CHAR NOT NULL,
state CHAR NOT NULL,
)";

if ($conn->query($sql) === TRUE) {
    echo "Table connections created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$conn->close();
?>
