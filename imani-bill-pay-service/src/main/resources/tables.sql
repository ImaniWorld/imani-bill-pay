create table GeographicalRegion (
       ID bigint not null auto_increment,
        RegionCode varchar(4) not null,
        RegionName varchar(100) not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    );


create table PlaidAPIInvocationStatistic (
       ID bigint not null auto_increment,
       PlaidAPIInvocation varchar(20),
        PlaidProduct varchar(20),
        UserRecordID bigint,
        ACHPaymentInfoID bigint,
        ClientID varchar(300),
        PublicToken varchar(300),
        AccountID varchar(300),
        DisplayMessage varchar(300),
        ErrorCode varchar(300),
        ErrorMessage varchar(300),
        ErrorType varchar(300),
        ItemID varchar(300),
        RequestID varchar(300),
        SuggestedAction varchar(300),
        ApiInvocationEndDate datetime,
        ApiInvocationStartDate datetime not null,
        primary key (ID)
    )

create table ACHPaymentInfo (
       ID bigint not null auto_increment,
       acctName varchar(100),
       OfficialAcctName varchar(250),
       AcctType varchar(50),
       AcctSubType varchar(50),
        FinancialInstitution varchar(100),
        IsPrimary TINYINT,
        PlaidAccessToken varchar(300) not null,
        PlaidAcctID varchar(100) not null,
        StripeBankAcctToken varchar(100) not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        PropertyManagerID bigint,
        PropertyOwnerID bigint,
        UserRecordID bigint,
        primary key (ID)
    )


create table ACHPaymentInfo (
        ID bigint not null auto_increment,
        PlaidBankAcctID varchar(200),
        PlaidBankAcctName varchar(200),
        PlaidBankAcctOfficialName varchar(200),
        PlaidAccessToken varchar(300) not null,
        PlaidBankAcctSubType varchar(100),
        PlaidBankAcctType varchar(100),
        StripeAccountHolderName varchar(200),
        StripeAccountHolderType varchar(100),
        StripeBankAcctStatus varchar(15),
        StripeBankName varchar(200),
        StripeBankCountry varchar(100),
        StripeBankCurrency varchar(10),
        StripeBankAcctID varchar(200),
        StripeBankAcctLast4Digits varchar(10),
        StripeObject varchar(200),
        StripeBankRoutingNumber varchar(10),
        UserRecordID bigint,
        PropertyManagerID bigint,
        PropertyOwnerID bigint,
        CreateDate datetime not null,
        ModifyDate datetime,
        IsPrimary TINYINT,
        primary key (ID)
    ) engine=MyISAM;


create table PropertyOwner (
       ID bigint not null auto_increment,
       UserRecordID bigint,
        BusinessName varchar(50),
        ACHPaymentInfoID bigint,
        PropertyID bigint,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )

create table PropertyManager (
       ID bigint not null auto_increment,
       name varchar(50) not null,
        Email varchar(100),
        MobilePhone bigint not null,
        Phone bigint,
        PreferredContactType varchar(10),
        CreateDate datetime not null,
        ModifyDate datetime,
        ACHPaymentInfoID bigint,
        PropertyInfoID bigint,
        primary key (ID)
    );


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