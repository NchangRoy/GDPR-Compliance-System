public class Main {
     public static void main(String[] args) throws Exception {


        String query="""
                -- Users table: data subjects
CREATE TABLE users (
    id INT PRIMARY KEY,
    username TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE
);

-- Groups table
CREATE TABLE groups (
    id INT PRIMARY KEY,
    title TEXT NOT NULL
);

-- Membership table: links users to groups
CREATE TABLE members (
    id INT PRIMARY KEY,
    uid INT NOT NULL,
    gid INT NOT NULL,
    FOREIGN KEY (uid) REFERENCES users(id),
    FOREIGN KEY (gid) REFERENCES groups(id)
    -- uid "owns" this row (i.e., the user owns their membership)
    -- gid "is owned" by members (variable ownership)
);

-- Sharing table: tracks file shares or resource shares
CREATE TABLE shares (
    id INT PRIMARY KEY,
    uid_owner INT NOT NULL,           -- the user who owns the shared item
    share_with INT,                   -- another user it's shared with
    share_with_group INT,             -- or a group it's shared with
    resource_name TEXT NOT NULL,      -- example payload
    FOREIGN KEY (uid_owner) REFERENCES users(id),
    FOREIGN KEY (share_with) REFERENCES users(id),
    FOREIGN KEY (share_with_group) REFERENCES groups(id)
);

                """;
        String sql = """
           -- Users table
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    password_hash VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Restaurants table
CREATE TABLE Restaurants (
    restaurant_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    address TEXT,
    phone VARCHAR(15),
    opening_hours VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Menu items table
CREATE TABLE MenuItems (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    restaurant_id INT,
    name VARCHAR(100),
    description TEXT,
    price DECIMAL(10, 2),
    available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (restaurant_id) REFERENCES Restaurants(restaurant_id)
);

-- Orders table
CREATE TABLE Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    restaurant_id INT,
    total_price DECIMAL(10, 2),
    status ENUM('Pending', 'Preparing', 'Delivered', 'Cancelled') DEFAULT 'Pending',
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (restaurant_id) REFERENCES Restaurants(restaurant_id)
);

-- Order items table
CREATE TABLE OrderItems (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    item_id INT,
    quantity INT,
    price DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (item_id) REFERENCES MenuItems(item_id)
);

-- Payments table
CREATE TABLE Payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    payment_method ENUM('Card', 'Cash', 'Wallet'),
    amount DECIMAL(10, 2),
    payment_status ENUM('Pending', 'Completed', 'Failed') DEFAULT 'Pending',
    payment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP       ,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

            """;
        String query1="""
                -- Table: Organization
CREATE TABLE Organization (
    organization_ID INT PRIMARY KEY IDENTITY(200,1),
    name VARCHAR(50) NOT NULL,
    contact_person VARCHAR(50),
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(25)
);

-- Table: Event_Type
CREATE TABLE Event_Type (
    event_type_ID INT PRIMARY KEY IDENTITY(200,1),
    event_type_name VARCHAR(50) NULL
);

-- Table: Venue
CREATE TABLE Venue (
    venue_ID INT PRIMARY KEY IDENTITY(300,1),
    capacity INT NOT NULL,
    address_line VARCHAR(60),
    city VARCHAR(30),
    state VARCHAR(30),
    postal_code VARCHAR(15),
    country VARCHAR(30),
    online_flag BIT
);

-- Table: Event
CREATE TABLE Event (
    event_ID INT PRIMARY KEY IDENTITY(1,1),
    event_type_ID INT NOT NULL,
    organization_ID INT NOT NULL,
    venue_ID INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    budget_estimated MONEY,
    total_expenditure MONEY,
    description TEXT,
    status VARCHAR(20),
    estimated_attendance INT,
    actual_attendance INT,
    FOREIGN KEY (event_type_ID) REFERENCES Event_Type(event_type_ID),
    FOREIGN KEY (organization_ID) REFERENCES Organization(organization_ID),
    FOREIGN KEY (venue_ID) REFERENCES Venue(venue_ID)
);

-- Table: Partner
CREATE TABLE Partner (
    partner_ID INT PRIMARY KEY IDENTITY(500,1),
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(25)
);

-- Table: Event_Partner (junction)
CREATE TABLE Event_Partner (
    event_ID INT NOT NULL,
    partner_ID INT NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (event_ID, partner_ID),
    FOREIGN KEY (event_ID) REFERENCES Event(event_ID),
    FOREIGN KEY (partner_ID) REFERENCES Partner(partner_ID)
);

-- Table: Employee
CREATE TABLE Employee (
    employee_ID INT PRIMARY KEY IDENTITY(1,1),
    organization_ID INT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    job_title VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    FOREIGN KEY (organization_ID) REFERENCES Organization(organization_ID)
);

-- Table: Event_Employee (junction)
CREATE TABLE Event_Employee (
    event_ID INT NOT NULL,
    employee_ID INT NOT NULL,
    task VARCHAR(120),
    start_date DATE,
    deadline DATE,
    task_completed BIT,
    PRIMARY KEY (event_ID, employee_ID),
    FOREIGN KEY (event_ID) REFERENCES Event(event_ID),
    FOREIGN KEY (employee_ID) REFERENCES Employee(employee_ID)
);

-- Table: Attendee
CREATE TABLE Attendee (
    attendee_ID INT PRIMARY KEY IDENTITY(1,1),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(25)
);

-- Table: Event_Tickets (ticket templates for events)
CREATE TABLE Event_Tickets (
    ticket_ID VARCHAR(32) PRIMARY KEY,
    event_ID INT NOT NULL,
    ticket_type VARCHAR(50),
    price MONEY,
    FOREIGN KEY (event_ID) REFERENCES Event(event_ID)
);

-- Table: Event_Ticket_Assignment (actual assigned/purchased tickets)
CREATE TABLE Event_Ticket_Assignment (
    ticket_instance_ID UNIQUEIDENTIFIER DEFAULT NEWID() PRIMARY KEY,
    attendee_ID INT,
    event_ID INT,
    ticket_ID VARCHAR(32),
    purchase_date DATETIME,
    expiry_date DATETIME,
    price MONEY,
    ticket_type VARCHAR(50),
    FOREIGN KEY (attendee_ID) REFERENCES Attendee(attendee_ID),
    FOREIGN KEY (event_ID) REFERENCES Event(event_ID),
    FOREIGN KEY (ticket_ID) REFERENCES Event_Tickets(ticket_ID)
);

                """;


        JSqlParser parser=new JSqlParser();
        Graph graph=new Graph(parser.Parser(query));
        graph.annotate("users");


        /*
           Graph graph=new Graph(parser.Parser(query1));
           graph.annotate("Attendee");


           Graph graph=new Graph(parser.Parser(sql));
           graph.annotate("Users");

         */

    }
}
