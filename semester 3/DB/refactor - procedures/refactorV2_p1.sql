
CREATE OR ALTER PROCEDURE from_OneN_to_NOne (
	@TableN		VARCHAR(100),
	@FK_TableN	VARCHAR(100),
	@TableOne	VARCHAR(100)
) AS
BEGIN
	-- geting the PK's; the FK in T_One will have the name of PK_N
    DECLARE @PK_One VARCHAR(100), @PK_N VARCHAR(100);
    EXEC get_PK_name @TableOne, @PKColumn = @PK_One OUTPUT;
    EXEC get_PK_name @TableN, @PKColumn = @PK_N OUTPUT;

    -- mapping the PK-FK relations that are in the data base
    DECLARE @sql NVARCHAR(MAX);

    IF NOT EXISTS (SELECT 1 FROM sys.tables WHERE name = '#TempMap')
	BEGIN
		CREATE TABLE #TempMap (
			KeyIdN      INT,
			KeyIdOne    INT
		);
	END

    SET @sql = N'
        INSERT INTO #TempMap (KeyIdN, KeyIdOne)
        SELECT
            t_N.' + @PK_N + ' AS KeyIdN,
            t_O.' + @PK_One + ' AS KeyIdOne
        FROM ' + @TableOne + ' t_O
        JOIN (
            SELECT
                t_N.' + @FK_TableN + ' AS FK_One,
                MAX(t_N.' + @PK_N + ') AS Max_PK_N
            FROM ' + @TableN + ' t_N
            GROUP BY t_N.' + @FK_TableN + '
        ) AS grp
            ON grp.FK_One = t_O.' + @PK_One + '
        JOIN ' + @TableN + ' t_N
            ON t_N.' + @PK_N + ' = grp.Max_PK_N;
    ';
    PRINT(@sql); EXEC(@sql);
    
    EXEC (N'SELECT * FROM #TempMap');
    -- adding the new FK in T_One
    EXEC add_FK_to_Table @TableOne, @TableN, @PK_N;

    -- removing the FK from T_N
    DECLARE @FKName VARCHAR(100)
	EXEC remove_FK_from_table @TableN, @FK_TableN;

    -- adding the FK in TableOne
    SET @sql = N'
        UPDATE t_O
        SET t_O.' + @PK_N + ' = M.KeyIdN
        FROM ' + @TableOne + ' t_O
        JOIN #TempMap M
            ON M.KeyIdOne = t_O.' + @PK_One + ';
    ';
    PRINT(@sql); EXEC(@sql);
END;
GO
