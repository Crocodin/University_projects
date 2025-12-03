
CREATE OR ALTER PROCEDURE get_PK_name (
    @TableName  VARCHAR(100),
    @PKColumn   VARCHAR(100) OUTPUT
) AS
BEGIN
    SELECT @PKColumn = c.name
    FROM sys.tables t
    JOIN sys.indexes i 
        ON t.object_id = i.object_id
        AND i.is_primary_key = 1
    -- ^ this gives us the PK object id
    JOIN sys.index_columns ic
        ON i.object_id = ic.object_id
        AND i.index_id = ic.index_id
    -- ^ this gives us the index id of the PK
    JOIN sys.all_columns c
        ON ic.object_id = c.object_id
        AND ic.column_id = c.column_id
    -- ^ this associates the index with the column
    WHERE t.name = @TableName;
END;
GO

CREATE OR ALTER PROCEDURE add_FK_to_Table (
	@Table	VARCHAR(100),
    @FTable VARCHAR(100),
	@FKey	VARCHAR(100)
) AS
BEGIN
	DECLARE @sql NVARCHAR(MAX)

    SET @sql = N'
        ALTER TABLE ' + @Table + ' ADD ' + @FKey + ' INT NULL;
    ';
    EXEC(@sql);

    SET @sql = N'
        ALTER TABLE ' + @Table + ' ADD CONSTRAINT FK_' + @FKey + '  FOREIGN KEY ('
            + @FKey + '
        ) REFERENCES ' + @FTable + '(' + @FKey + ');
    ';
    EXEC(@sql);
END;
GO

CREATE OR ALTER PROCEDURE get_FK_name (
	@TableName  VARCHAR(100),
	@FKColumn	VARCHAR(100),
	@FKName		VARCHAR(100) OUTPUT
) AS
BEGIN
	SELECT TOP 1 @FKName = fk.name
	FROM sys.foreign_keys fk
	JOIN sys.foreign_key_columns fkc
		ON fk.object_id = fkc.constraint_object_id
	WHERE fk.parent_object_id = OBJECT_ID(@TableName)
	  AND COL_NAME(fkc.parent_object_id, fkc.parent_column_id) = @FKColumn;
END;
GO

CREATE OR ALTER PROCEDURE remove_FK_from_table (
	@Table	VARCHAR(100),
	@FKey	VARCHAR(100)
) AS
BEGIN
	DECLARE @sql VARCHAR(MAX);
	DECLARE @FKName VARCHAR(100);
	EXEC get_FK_name @Table, @FKey, @FKName = @FKName OUTPUT;

	SET @sql = N' 
		ALTER TABLE ' + @Table + ' DROP CONSTRAINT ' + @FKName + '; 
	';
	PRINT(@sql); EXEC(@sql);

    SET @sql = N'
		ALTER TABLE ' + @Table + ' DROP COLUMN ' + @FKey + ';
    ';
	PRINT(@sql); EXEC(@sql);
END;
GO