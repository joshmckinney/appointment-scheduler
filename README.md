# Appointment Scheduler

A JavaFX CRM application to manage customer and appointment details.

### Features:
- Create, modify, and delete appointments and customers
- Ability to add mulitple consultants
- Soft delete customers and view by active or inactive
- Multiple calendar options to view by monthly, weekly, daily, or all
- Report generation
	- Weekly Appointments Count
	- Monthly Appointments by Type
	- Appointments by consultant

### Requirements:
- Java 8
- MariaDB or MySQL *(configure your settings in utils/Database.java)*

### Database Setup:
1. Create a new database with your desired settings and replace username and database below
2. Build out database from schema dump:
`mysql -u username -p databasename < db_schema.sql`
3. Populate with data (modify users or data if you wish):
`mysql -u username -p databasename < db_init.sql`
