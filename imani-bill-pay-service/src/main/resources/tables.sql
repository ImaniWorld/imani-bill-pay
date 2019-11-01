create table MonthlyRentalBill (
       ID bigint not null auto_increment,
       RentalMonth datetime not null,
       RentalAgreementID bigint not null,
       UserResidenceID bigint not null,
       AmountPaid double precision,
       BillClosed TINYINT,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    );


create table MonthlyRentalFee (
       ID bigint not null auto_increment,
        FeeName varchar(50) not null,
        FeeDescription varchar(250),
        FeePaymentChargeType varchar(25) not null,
        OptionalFlatAmount double precision,
        optionalFlatRate double precision,
        RentalFeeType varchar(25) not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        PropertyID bigint,
        primary key (ID)
    );



create table PropertyService (
       ID bigint not null auto_increment,
        ServiceName varchar(100) not null,
        ServiceMonthlyCost double precision not null,
        ServiceActive TINYINT,
        PropertyID bigint not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    );


    create table UserResidencePropertyService (
       ID bigint not null auto_increment,
       PropertyServicesID bigint not null,
        UserResidenceID bigint not null,
        ServiceActive TINYINT,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    );

    create table LeaseAgreement (
           ID bigint not null auto_increment,
           AgreementDocument varchar(100),
           LeaseAgreementType varchar(20) not null,
           MonthlyRentalCost double precision not null,
            AgreementInEffect TINYINT,
            EffectiveDate datetime not null,
            PropertyManagerAcceptanceDate datetime,
            PropertyManagerAcceptedAgreement TINYINT,
            PropertyOwnerAcceptanceDate datetime,
            PropertyOwnerAcceptedAgreement TINYINT,
            TenantAcceptanceDate datetime,
            TenantAcceptedAgreement TINYINT,
            TerminationDate datetime not null,
            ApartmentID bigint,
            PropertyID bigint,
            PropertyManagerID bigint,
            PropertyOwnerID bigint,
            UserRecordID bigint not null,
            CreateDate datetime not null,
            ModifyDate datetime,
            primary key (ID)
        );


  create table BillPayInquiry (
         ID bigint not null auto_increment,
         BusinessType varchar(100) not null,
          ContactName varchar(100) not null,
          ContactEmail varchar(50) not null,
          ContactPhone bigint,
          ContactQuestion varchar(300) not null,
          NumberOfUsers integer not null,
          CreateDate datetime not null,
          ModifyDate datetime,
          primary key (ID)
      );