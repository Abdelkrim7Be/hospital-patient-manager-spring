# 🏥 Hospital Management System - Login Information

## 🔐 Security Authentication

The application is now running with **initial data loaded** and **security fully configured**.

### 👥 User Credentials

**All users have the same password: `1234`**

| Username | Roles       | Access Level | Description                             |
| -------- | ----------- | ------------ | --------------------------------------- |
| `admin`  | USER, ADMIN | Full Access  | Can view, create, edit, delete patients |
| `user1`  | USER        | View Only    | Can view patients and dashboard         |
| `user2`  | USER        | View Only    | Can view patients and dashboard         |

### 🌐 Application URLs

| Page                    | URL                                      | Access Required        |
| ----------------------- | ---------------------------------------- | ---------------------- |
| **Main Application**    | http://localhost:8080                    | Any authenticated user |
| **Patient List**        | http://localhost:8080/user/index         | USER role              |
| **Dashboard**           | http://localhost:8080/user/dashboard     | USER role              |
| **Add New Patient**     | http://localhost:8080/admin/formPatients | ADMIN role             |
| **Test Info Page**      | http://localhost:8080/user/test-info     | USER role              |
| **H2 Database Console** | http://localhost:8080/h2-console         | No auth (dev only)     |

### 🗄️ Database Information (H2 Console)

| Setting          | Value                     |
| ---------------- | ------------------------- |
| **JDBC URL**     | `jdbc:h2:mem:hospital_db` |
| **Username**     | `sa`                      |
| **Password**     | _(empty)_                 |
| **Driver Class** | `org.h2.Driver`           |

## 📊 Initial Data Summary

✅ **38 Test Patients** loaded with diverse medical scenarios:

- **30 Active** patients (normal operation)
- **3 Discharged** patients (completed treatment)
- **2 Inactive** patients (transferred)
- **1 Suspended** patient (administrative)

✅ **Medical Categories**:

- Emergency patients with various conditions
- Pediatric patients (children)
- Geriatric patients (elderly)
- Chronic disease patients
- Recovery patients
- Medical staff test data

✅ **Data Ranges**:

- Ages: 6-90 years old
- Scores: 0-1000 (testing all boundaries)
- Health status: Both sick and healthy patients
- All patient status types represented

## 🔧 Quick Start Guide

1. **Access the application**: http://localhost:8080
2. **Login** with any of the credentials above
3. **Explore features**:
   - View patient list with search and pagination
   - Use dashboard to see statistics
   - Admin users can add/edit/delete patients
   - Test different user roles by logging in with different accounts

## 🛠️ Development Features

- **Security**: Spring Security with role-based access
- **Database**: H2 in-memory with comprehensive test data
- **UI**: Hospital-themed responsive design
- **Search**: Advanced filtering and pagination
- **Validation**: Form validation with error handling
- **Statistics**: Real-time dashboard with patient metrics

---

_Hospital Management System - Ready for Testing & Development_ 🚀
