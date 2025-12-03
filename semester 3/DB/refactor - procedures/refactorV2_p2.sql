
CREATE OR ALTER PROCEDURE from_OneN_To_MN (
	@TableN		VARCHAR(100),
	@FK_TableN	VARCHAR(100),
	@TableOne	VARCHAR(100)
) AS
BEGIN
	-- we need the PK of the tables
	DECLARE @PK_One VARCHAR(100), @PK_N VARCHAR(100);

    EXEC get_PK_name @TableOne, @PKColumn = @PK_One OUTPUT;
    EXEC get_PK_name @TableN, @PKColumn = @PK_N OUTPUT;

	-- we create the new table
	DECLARE @sql NVARCHAR(MAX);
	DECLARE @NtoMTable VARCHAR(201);
	SET @NtoMTable = @TableN + '_' + @TableOne

	SET @sql = N'
		CREATE TABLE ' + @NtoMTable + ' (
			' + @PK_N + ' INT FOREIGN KEY REFERENCES ' + @TableN + '(' + @PK_N + '),
			' + @PK_One + ' INT FOREIGN KEY REFERENCES ' + @TableOne + '(' + @PK_One + '),
			CONSTRAINT PK_' + @PK_N +  '_' + @PK_One + ' PRIMARY KEY (' +@PK_N + ', ' + @PK_One + ')
		);
	';
	PRINT(@sql); EXEC(@sql);

	-- now inserting the values
	SET @sql = N'
		INSERT INTO ' + @NtoMTable + '(' +@PK_N + ', ' + @PK_One + ')
		SELECT
			t_N.' + @PK_N + ',
			t_O.' + @PK_One + '
		FROM ' + @TableN + ' t_N 
		JOIN ' + @TableOne + ' t_O
			ON t_N.' + @FK_TableN + ' = t_O.' + @PK_One + ';
	';
	PRINT(@sql); EXEC(@sql);
	
	-- now deleting the FK from T_N
	EXEC remove_FK_from_table @TableN, @FK_TableN
END;
GO

