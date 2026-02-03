CREATE TABLE absence
(
    absence_id       BIGINT                  NOT NULL,
    reference_number VARCHAR(255) DEFAULT '' NOT NULL,
    employee_id      BIGINT NULL,
    start_date       date NULL,
    end_date         date NULL,
    total_days DOUBLE NULL,
    issue_date       datetime NULL,
    type             VARCHAR(255) NULL,
    CONSTRAINT pk_absence PRIMARY KEY (absence_id)
);

CREATE TABLE absence_request
(
    absence_request_id       BIGINT                  NOT NULL,
    reference_number         VARCHAR(255) DEFAULT '' NOT NULL,
    employee_id              BIGINT NULL,
    start_date               date NULL,
    end_date                 date NULL,
    total_days DOUBLE NULL,
    issue_date               datetime NULL,
    type                     VARCHAR(255) NULL,
    approved_by_manager      BIT(1) NULL,
    approved_by_hr           BIT(1) NULL,
    status                   VARCHAR(255) NULL,
    medical_certificate_path VARCHAR(255) DEFAULT '' NULL,
    CONSTRAINT pk_absencerequest PRIMARY KEY (absence_request_id)
);

CREATE TABLE document_requests
(
    request_id       BIGINT                  NOT NULL,
    reference_number VARCHAR(255) DEFAULT '' NOT NULL,
    documents        VARCHAR(255) NULL,
    employee_id      BIGINT                  NOT NULL,
    status           VARCHAR(255) NULL,
    request_date     datetime NULL,
    CONSTRAINT pk_document_requests PRIMARY KEY (request_id)
);

CREATE TABLE employee_balance
(
    balance_id        BIGINT NOT NULL,
    year              INT NULL,
    annual_balance DOUBLE NULL,
    current_balance DOUBLE NULL,
    monthly_balance DOUBLE NULL,
    accumulated_balance DOUBLE NULL,
    used_balance DOUBLE NULL,
    remainder_balance FLOAT NULL,
    last_updated      datetime NULL,
    employee_id       BIGINT NULL,
    CONSTRAINT pk_employee_balance PRIMARY KEY (balance_id)
);

CREATE TABLE employee_roles
(
    employee_id BIGINT NOT NULL,
    role_id     BIGINT NOT NULL
);

CREATE TABLE employees
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    entity        VARCHAR(255) NULL,
    occupation    VARCHAR(255) NULL,
    password      VARCHAR(255) NULL,
    matriculation VARCHAR(255) NOT NULL,
    join_date     date NULL,
    balance_id    BIGINT NULL,
    status        VARCHAR(255) NULL,
    managed_by    BIGINT NULL,
    CONSTRAINT pk_employees PRIMARY KEY (id)
);

CREATE TABLE expense_items
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    designation  VARCHAR(255) NOT NULL,
    amount DOUBLE NOT NULL,
    expense_date date         NOT NULL,
    expense_id   BIGINT       NOT NULL,
    invoiced     BIT(1)       NOT NULL,
    CONSTRAINT pk_expense_items PRIMARY KEY (id)
);

CREATE TABLE expenses
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    employee_id      BIGINT   NOT NULL,
    issue_date       date     NOT NULL,
    total_amount DOUBLE NOT NULL,
    currency         VARCHAR(255) NULL,
    expense_location VARCHAR(255) NULL,
    motif            VARCHAR(255) NULL,
    balance DOUBLE NULL,
    created_at       datetime NOT NULL,
    CONSTRAINT pk_expenses PRIMARY KEY (id)
);

CREATE TABLE holidays
(
    id          BIGINT AUTO_INCREMENT          NOT NULL,
    name        VARCHAR(255) NOT NULL,
    start_date  date         NOT NULL,
    end_date    date         NOT NULL,
    status      VARCHAR(255) DEFAULT 'PENDING' NULL,
    type        VARCHAR(255) NULL,
    floating    BIT(1)       NOT NULL,
    leave_days  INT NULL,
    last_update datetime NULL,
    CONSTRAINT pk_holidays PRIMARY KEY (id)
);

CREATE TABLE leave_balance_adjustment
(
    leave_adjustment_id BIGINT NOT NULL,
    employee_id         BIGINT NULL,
    leave_leave_id      BIGINT NULL,
    delta DOUBLE NOT NULL,
    reason              VARCHAR(255) NULL,
    created_at          datetime NULL,
    CONSTRAINT pk_leavebalanceadjustment PRIMARY KEY (leave_adjustment_id)
);

CREATE TABLE leave_requests
(
    leave_request_id         BIGINT                  NOT NULL,
    reference_number         VARCHAR(255) DEFAULT '' NOT NULL,
    employee_id              BIGINT                  NOT NULL,
    start_date               date                    NOT NULL,
    end_date                 date                    NOT NULL,
    total_days DOUBLE NULL,
    request_date             datetime                NOT NULL,
    type_of_leave            VARCHAR(255)            NOT NULL,
    type_details             VARCHAR(255) NULL,
    status                   VARCHAR(255) NULL,
    approved_by_manager      BIT(1) NULL,
    approved_by_hr           BIT(1) NULL,
    medical_certificate_path VARCHAR(255) NULL,
    comment                  VARCHAR(255) DEFAULT '' NULL,
    CONSTRAINT pk_leave_requests PRIMARY KEY (leave_request_id)
);

CREATE TABLE leaves
(
    leave_id         BIGINT                  NOT NULL,
    reference_number VARCHAR(255) DEFAULT '' NOT NULL,
    employee_id      BIGINT                  NOT NULL,
    from_date        date                    NOT NULL,
    leave_type       VARCHAR(255)            NOT NULL,
    to_date          date                    NOT NULL,
    total_days DOUBLE NULL,
    CONSTRAINT pk_leaves PRIMARY KEY (leave_id)
);

CREATE TABLE loan_requests
(
    request_id                     BIGINT                  NOT NULL,
    reference_number               VARCHAR(255) DEFAULT '' NOT NULL,
    employee_id                    BIGINT NULL,
    amount DOUBLE NULL,
    issue_date                     datetime NULL,
    type                           VARCHAR(255) NULL,
    approved_by_hr_department      BIT(1) NULL,
    approved_by_finance_department BIT(1) NULL,
    motif                          VARCHAR(255) NULL,
    status                         VARCHAR(255) NULL,
    CONSTRAINT pk_loan_requests PRIMARY KEY (request_id)
);

CREATE TABLE loans
(
    loan_id       BIGINT NOT NULL,
    employee_id   BIGINT NULL,
    issue_date    datetime NULL,
    amount DOUBLE NULL,
    type          VARCHAR(255) NULL,
    approval_date datetime NULL,
    CONSTRAINT pk_loans PRIMARY KEY (loan_id)
);

CREATE TABLE payrolls_history
(
    history_id          BIGINT   NOT NULL,
    payroll_month       INT      NOT NULL,
    payroll_year        INT      NOT NULL,
    execution_date      datetime NOT NULL,
    number_of_employees INT      NOT NULL,
    succeeded_payrolls  INT      NOT NULL,
    failed_payrolls     INT      NOT NULL,
    total_payrolls      INT      NOT NULL,
    status              VARCHAR(255) NULL,
    CONSTRAINT pk_payrolls_history PRIMARY KEY (history_id)
);

CREATE TABLE roles
(
    role_id   BIGINT       NOT NULL,
    role_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (role_id)
);

ALTER TABLE absence
    ADD CONSTRAINT uc_absence_reference_number UNIQUE (reference_number);

ALTER TABLE absence_request
    ADD CONSTRAINT uc_absencerequest_reference_number UNIQUE (reference_number);

ALTER TABLE document_requests
    ADD CONSTRAINT uc_document_requests_reference_number UNIQUE (reference_number);

ALTER TABLE employee_balance
    ADD CONSTRAINT uc_employee_balance_employee UNIQUE (employee_id);

ALTER TABLE employees
    ADD CONSTRAINT uc_employees_balance UNIQUE (balance_id);

ALTER TABLE employees
    ADD CONSTRAINT uc_employees_email UNIQUE (email);

ALTER TABLE employees
    ADD CONSTRAINT uc_employees_matriculation UNIQUE (matriculation);

ALTER TABLE holidays
    ADD CONSTRAINT uc_holidays_name UNIQUE (name);

ALTER TABLE leave_requests
    ADD CONSTRAINT uc_leave_requests_reference_number UNIQUE (reference_number);

ALTER TABLE leaves
    ADD CONSTRAINT uc_leaves_reference_number UNIQUE (reference_number);

ALTER TABLE loan_requests
    ADD CONSTRAINT uc_loan_requests_reference_number UNIQUE (reference_number);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_role_name UNIQUE (role_name);

ALTER TABLE absence_request
    ADD CONSTRAINT FK_ABSENCEREQUEST_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE absence
    ADD CONSTRAINT FK_ABSENCE_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE document_requests
    ADD CONSTRAINT FK_DOCUMENT_REQUESTS_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE employees
    ADD CONSTRAINT FK_EMPLOYEES_ON_BALANCE FOREIGN KEY (balance_id) REFERENCES employee_balance (balance_id);

ALTER TABLE employees
    ADD CONSTRAINT FK_EMPLOYEES_ON_MANAGED_BY FOREIGN KEY (managed_by) REFERENCES employees (id);

ALTER TABLE employee_balance
    ADD CONSTRAINT FK_EMPLOYEE_BALANCE_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE expenses
    ADD CONSTRAINT FK_EXPENSES_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE expense_items
    ADD CONSTRAINT FK_EXPENSE_ITEMS_ON_EXPENSE FOREIGN KEY (expense_id) REFERENCES expenses (id);

ALTER TABLE leave_balance_adjustment
    ADD CONSTRAINT FK_LEAVEBALANCEADJUSTMENT_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE leave_balance_adjustment
    ADD CONSTRAINT FK_LEAVEBALANCEADJUSTMENT_ON_LEAVE_LEAVEID FOREIGN KEY (leave_leave_id) REFERENCES leaves (leave_id);

ALTER TABLE leaves
    ADD CONSTRAINT FK_LEAVES_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE leave_requests
    ADD CONSTRAINT FK_LEAVE_REQUESTS_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE loans
    ADD CONSTRAINT FK_LOANS_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE loan_requests
    ADD CONSTRAINT FK_LOAN_REQUESTS_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE employee_roles
    ADD CONSTRAINT fk_emprol_on_employee FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE employee_roles
    ADD CONSTRAINT fk_emprol_on_role FOREIGN KEY (role_id) REFERENCES roles (role_id);