
CREATE OR ALTER PROCEDURE from_MN_to_OneN (
	@TableN		VARCHAR(100),		-- this one will become One
	@TableM		VARCHAR(100),       -- this one will become Many
	@TableN_M	VARCHAR(201)        -- (m:n)
) AS
BEGIN
	-- we need the PK's of the tables
    DECLARE @PK_N VARCHAR(100), @PK_M VARCHAR(100), @PK_N_M VARCHAR(100);
    
    EXEC get_PK_name @TableN, @PKColumn = @PK_N OUTPUT;
    EXEC get_PK_name @TableM, @PKColumn = @PK_M OUTPUT;
    EXEC get_PK_name @TableN_M, @PKColumn = @PK_N_M OUTPUT;

	-- adding the FK to T_N
    EXEC add_FK_to_Table @TableN, @TableM, @PK_M;

    -- adding values to the new FK
    DECLARE @sql NVARCHAR(MAX);

    SET @sql = N'
        UPDATE t_N
        SET t_N.' + @PK_M + ' = src.M_id
        FROM ' + @TableN + ' t_N
        JOIN (
            SELECT
                t_NM.' + @PK_N + ' AS N_id,
                MAX(t_NM.' + @PK_M + ') AS M_id
            FROM ' + @TableN_M + ' t_NM
            GROUP BY t_NM.' + @PK_N + '
        ) AS src
            ON t_N.' + @PK_N + ' = src.N_id;
    ';
    PRINT(@sql); EXEC(@sql);

    -- droping the old table
    SET @sql = N'
        DROP TABLE ' + @TableN_M + ';
    ';
    PRINT(@sql); EXEC(@sql);
END;
GO