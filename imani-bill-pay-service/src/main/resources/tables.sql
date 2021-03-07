create table GeographicalRegion (
       ID bigint not null auto_increment,
        RegionCode varchar(4) not null,
        RegionName varchar(100) not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    );


create table Address (
       ID bigint not null auto_increment,
        StreetAddress varchar(400) not null,
        CityID bigint not null,
        ZipCode varchar(50) not null,
        POBoxNumber varchar(50),
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )

create table Community (
       ID bigint not null auto_increment,
        CommunityName varchar(400),
        CommunityTypeE varchar(25) not null,
        ManagedByBusinessID bigint,
        CityID bigint not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table UserToBusiness (
       ID bigint not null auto_increment,
        UserRecordID bigint not null,
        BusinessID bigint not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table Business (
       ID bigint not null auto_increment,
        Name varchar(100) not null,
        BusinessTypeE varchar(25) not null,
        Email varchar(100),
        MobilePhone bigint not null,
        Phone bigint,
        PreferredContactType varchar(10),
        StripeAcctID varchar(100),
        PropertyID bigint,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table Property (
       ID bigint not null auto_increment,
       AddressID bigint not null,
        Block varchar(30),
        BIN varchar(30),
        Latitude double precision,
        Longitude double precision,
        Lot varchar(30),
        PropertyTypeE varchar(20) not null,
        MthlyNumberOfDaysPaymentLate integer,
        CommunityID bigint,
        OwnerID bigint,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table TuitionGrade (
       ID bigint not null auto_increment,
        Grade varchar(100) not null,
        Description varchar(200),
        TuitionGradeTypeE varchar(25) not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table SchoolToTuitionGrade (
       ID bigint not null auto_increment,
        NumberOfDaysTillLate integer,
        BusinessID bigint not null,
        TuitionGradeID bigint not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )



create table TuitionAgreement (
       ID bigint not null auto_increment,
        StudentUserRecordID bigint not null,
        TuitionGradeID bigint not null,
        AgreementInForce TINYINT,
        BillScheduleTypeE varchar(20) not null,
        NumberOfDaysTillLate integer,
        FixedCost double precision not null,
        AgreementDocument varchar(100),
        BusinessID bigint not null,
        UserRecordID bigint not null,
        EffectiveDate datetime not null,
        TerminationDate datetime not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )

create table UtilityServiceArea (
       ID bigint not null auto_increment,
       AreaName varchar(400),
        AreaDescription varchar(400),
        UtilityTypeE varchar(25) not null,
        Active TINYINT,
        PropertyID bigint,
        BusinessID bigint,
        CommunityID bigint,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )

create table WaterServiceAgreement (
       ID bigint not null auto_increment,
       UtilityProviderBusinessID bigint not null,
       SvcCustomerAcctID varchar(100),
       SvcDescription varchar(300),
       UtilityServiceAreaID bigint,
        FixedCost double precision,
        NbrOfGallonsPerFixedCost bigint,
        NumberOfDaysTillLate integer,
        BillScheduleTypeE varchar(20) not null,
        AgreementInForce TINYINT,
        EffectiveDate datetime not null,
        TerminationDate datetime,
        AgreementDocument varchar(100),
        AgreementUserRecordID bigint not null,
        AgreementPropertyID bigint,
        AgreementBusinessID bigint,
        AgreementCommunityID bigint,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )

create table SewerServiceAgreement (
       ID bigint not null auto_increment,
        UtilityProviderBusinessID bigint not null,
        SvcCustomerAcctID varchar(100),
        SvcDescription varchar(300),
        UtilityServiceAreaID bigint,
        FixedCost double precision,
        NumberOfDaysTillLate integer,
        BillScheduleTypeE varchar(20) not null,
        AgreementInForce TINYINT,
        EffectiveDate datetime not null,
        TerminationDate datetime,
        AgreementDocument varchar(100),
        AgreementUserRecordID bigint not null,
        AgreementPropertyID bigint,
        AgreementBusinessID bigint,
        AgreementCommunityID bigint,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )

create table AgreementToScheduleBillPayFee (
       ID bigint not null auto_increment,
        Enforced TINYINT,
        SewerServiceAgreementID bigint,
        WaterServiceAgreementID bigint,
        BillPayFeeID bigint,
        primary key (ID)
    )

create table WaterUtilization (
       ID bigint not null auto_increment,
        NumberOfGallonsUsed bigint,
        Description varchar(200),
        WaterServiceAgreementID bigint not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table WaterUtilizationCharge (
       ID bigint not null auto_increment,
        Charge double precision,
        TotalGallonsUsed bigint,
        ImaniBillID bigint not null,
        UtilizationStartDate datetime not null,
        UtilizationEndDate datetime not null,
        primary key (ID)
    )

create table ChildCareAgreement (
       ID bigint not null auto_increment,
        ChildName varchar(100) not null,
        AgreementDocument varchar(100),
        AgreementInForce TINYINT,
        FixedCost double precision not null,
        NumberOfDaysTillLate integer,
        BillScheduleTypeE varchar(20) not null,
        DayCareID bigint not null,
        UserRecordID bigint not null,
        EffectiveDate datetime not null,
        TerminationDate datetime not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table PropertyLeaseAgreement (
       ID bigint not null auto_increment,
        LeasedApartmentID bigint,
        LeasedPropertyID bigint,
        AgreementDocument varchar(100),
        AgreementInForce TINYINT,
        FixedCost double precision not null,
        NumberOfDaysTillLate integer,
        BillScheduleTypeE varchar(20) not null,
        UserRecordID bigint not null,
        EffectiveDate datetime not null,
        TerminationDate datetime not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table ImaniBill (
       ID bigint not null auto_increment,
        AmountOwed double precision,
        AmountPaid double default 0,
        BillScheduleDate datetime,
        BillScheduleTypeE varchar(25) not null,
        BillServiceRenderedTypeE varchar(25) not null,
        WaterServiceAgreementID bigint,
        SewerServiceAgreementID bigint,
        TuitionAgreementID bigint,
        ChildCareAgreementID bigint,
        PropertyLeaseAgreementID bigint,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table BillPayFee (
       ID bigint not null auto_increment,
        FeeName varchar(50) not null,
        FeeDescription varchar(250),
        OptionalFlatAmount double precision,
        OptionalFlatRate double precision,
        FeeTypeE varchar(25) not null,
        FeePaymentChargeType varchar(25) not null,
        BillScheduleTypeE varchar(20),
        BillServiceRenderedTypeE varchar(25) not null,
        BusinessID bigint not null,
        CreateDate datetime not null,
        ModifyDate datetime,
        primary key (ID)
    )


create table PlaidAPIInvocationStatistic (
       ID bigint not null auto_increment,
       PlaidAPIInvocation varchar(20),
        PlaidProduct varchar(20),
        UserRecordID bigint,
        ACHPaymentInfoID bigint,
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
        StripeBankAcctToken varchar(200),
        StripeBankAcctID varchar(200),
        StripeAccountHolderName varchar(200),
        StripeAccountHolderType varchar(100),
        StripeBankAcctStatus varchar(15),
        StripeBankName varchar(200),
        StripeBankCountry varchar(100),
        StripeBankCurrency varchar(10),
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


create table PlaidBankAcctBalance (
        ID bigint not null auto_increment,
        Available double precision,
        AcctLimit double precision,
        UnOfficialCurrency varchar(255),
        CurrencyCode varchar(255),
        Current double precision,
        ACHPaymentInfoID bigint,
        CreateDate datetime not null,
        ModifyDate datetime,
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
       Name varchar(50) not null,
       Email varchar(100),
       MobilePhone bigint not null,
       Phone bigint,
       PreferredContactType varchar(10),
       StripeAcctID varchar(100),
       CreateDate datetime not null,
       ModifyDate datetime,
       ACHPaymentInfoID bigint,
       PropertyInfoID bigint,
       primary key (ID)
    ) engine=MyISAM;



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
