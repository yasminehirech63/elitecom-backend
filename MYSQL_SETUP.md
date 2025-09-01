# MySQL Setup Guide for EliteCom Backend

## Prerequisites
1. Install MySQL Server (version 8.0 or higher recommended)
2. Install MySQL Workbench (optional but recommended for database management)

## Installation Steps

### 1. Install MySQL Server

#### Windows:
- Download MySQL Installer from: https://dev.mysql.com/downloads/installer/
- Run the installer and follow the setup wizard
- Choose "Developer Default" or "Server only" installation
- Set root password during installation (remember this password!)

#### macOS:
```bash
brew install mysql
brew services start mysql
```

#### Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

### 2. Create Database and User (Optional but Recommended)

Connect to MySQL as root:
```bash
mysql -u root -p
```

Create database and user:
```sql
CREATE DATABASE elitecom CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'elitecom_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON elitecom.* TO 'elitecom_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Update Application Properties

If you created a separate user, update `src/main/resources/application.properties`:

```properties
spring.datasource.username=elitecom_user
spring.datasource.password=your_secure_password
```

### 4. Database Configuration Options

The current configuration uses:
- **URL**: `jdbc:mysql://localhost:3306/elitecom?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
- **DDL Auto**: `update` (tables will be created/updated automatically)
- **Dialect**: MySQL 8.0

### 5. Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

The application will:
1. Connect to MySQL
2. Create the database if it doesn't exist
3. Create/update tables based on your entities
4. Initialize sample data via DataInitializer

## Troubleshooting

### Connection Issues:
1. **Access denied**: Check username/password in application.properties
2. **Connection refused**: Ensure MySQL service is running
3. **Database not found**: The `createDatabaseIfNotExist=true` parameter should handle this

### Port Issues:
- Default MySQL port is 3306
- If using different port, update the URL in application.properties

### SSL Issues:
- The current configuration disables SSL (`useSSL=false`)
- For production, enable SSL and configure certificates

## Production Considerations

1. **Security**:
   - Use strong passwords
   - Enable SSL/TLS
   - Restrict database user privileges
   - Use connection pooling

2. **Performance**:
   - Configure connection pool size
   - Optimize MySQL configuration
   - Use appropriate indexes

3. **Backup**:
   - Set up regular database backups
   - Test restore procedures

## Migration from H2

The application will automatically:
- Create new MySQL tables based on your entities
- Initialize sample data via DataInitializer
- No manual data migration needed for this setup

## Verification

After running the application:
1. Check application logs for successful database connection
2. Verify tables are created in MySQL
3. Test API endpoints to ensure data persistence
