
CREATE PROCEDURE git @version INT AS
BEGIN
    DECLARE @current_version INT;
    SELECT @current_version = version_id FROM db_version;

    IF (@version = @current_version)
    BEGIN
        RAISERROR('This version is curently in use!', 16, 1);
        return
    END
    
    IF (@version < 0 or @version > 5)
    BEGIN
        RAISERROR('Invalid version input', 16, 1);
        return
    END

    PRINT 'Current version: ' + CAST(@current_version AS VARCHAR(10));
    PRINT 'Target version: ' + CAST(@version AS VARCHAR(10));

    IF (@version > @current_version)
    BEGIN
        WHILE (@current_version < @version)
        BEGIN
            SET @current_version = @current_version + 1;

            IF (@current_version = 1) EXEC alt_column_type;
            IF (@current_version = 2) EXEC alt_const_made_year;
            IF (@current_version = 3) EXEC add_tank_info;
            IF (@current_version = 4) EXEC add_column_country;
            IF (@current_version = 5) EXEC add_foreign_key;
        END
    END
    ELSE IF (@version < @current_version)
    BEGIN
        WHILE (@current_version > @version)
        BEGIN            
            IF (@current_version = 5) EXEC del_foreign_key;
            IF (@current_version = 4) EXEC del_column_country;
            IF (@current_version = 3) EXEC del_tank_info;
            IF (@current_version = 2) EXEC rev_const_made_year;
            IF (@current_version = 1) EXEC rev_column_type;

            SET @current_version = @current_version - 1;
        END
    END

    UPDATE db_version SET version_id = @current_version;

    PRINT 'Database base is now at version ' + CAST(@current_version AS VARCHAR(10));
END;
GO
