# Employee Performance Management System

A JavaServer Faces (JSF) and Hibernate-based web application for managing employee performance reviews.

## Features

- **User Authentication**: Secure login system
- **Employee Management**: Add and view employee records
- **Performance Reviews**: Submit and track employee performance reviews
- **Search & Filter**: Find employees using HQL queries
- **Input Validation**: JSF validators ensure data consistency
- **Custom Events**: Notification system for review submissions
- **Data Persistence**: Hibernate ORM with relational mapping

## Technology Stack

- JavaServer Faces (JSF) 2.3
- Hibernate ORM 5.6.x
- MySQL Database
- Maven for dependency management
- Apache Tomcat 9.x

## Setup Instructions

### Prerequisites

- JDK 8 or higher
- Apache Maven 3.6+
- MySQL Server 5.7+
- Apache Tomcat 9.x
- VS Code with Java Extension Pack

### Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE performance_db;
```

2. Update database credentials in `hibernate.cfg.xml`:
```xml
<property name="hibernate.connection.username">your_username</property>
<property name="hibernate.connection.password">your_password</property>
```

### Build and Deploy

1. Clone the repository:
```bash
git clone <your-repo-url>
cd employee-performance-system
```

2. Build the project:
```bash
mvn clean install
mvn jetty:run
```


4. Access the application:
```
http://localhost:8080/performance-system/login.xhtml
```

### Default Login Credentials

- Username: `admin`
- Password: `admin`

## Application Flow

1. **Login** (`login.xhtml`)
   - User enters credentials
   - LoginBean validates and navigates to dashboard

2. **Dashboard** (`dashboard.xhtml`)
   - Displays all employees
   - Shows latest review for each employee
   - Search functionality with filters
   - Navigate to Add Employee or Performance Review

3. **Add Employee** (`addEmployee.xhtml`)
   - Form with validation
   - Email validator ensures proper format
   - Salary validation for positive values
   - Creates employee and returns to dashboard

4. **Performance Review** (`performanceReview.xhtml`)
   - Select employee from dropdown
   - Rate performance using custom converter
   - Submit triggers custom event
   - Notification displayed on submission

## Key Technical Features

### JSF Components

- **Validators**: Custom email validator for employee emails
- **Converters**: PerformanceRating enum converter
- **Events**: Custom ReviewSubmittedEvent with listener
- **Navigation**: Declarative navigation rules in faces-config.xml

### Hibernate Features

- **Entity Mapping**: One-to-Many relationship (Employee → Reviews)
- **Cascade Operations**: Reviews cascade with employee deletion
- **HQL Queries**: Search employees by name or department
- **Session Management**: HibernateUtil for session factory

### Validation Rules

- Employee name: Required, 2-50 characters
- Email: Required, valid email format
- Salary: Required, positive number
- Review rating: Required enum value
- Review comments: Required, max 500 characters

## Development Notes

- Sessions are managed per request
- All database operations use transactions
- Form validation occurs on server-side
- Custom events propagate through JSF lifecycle
- Hibernate auto-generates schema on startup (hbm2ddl.auto=update)
