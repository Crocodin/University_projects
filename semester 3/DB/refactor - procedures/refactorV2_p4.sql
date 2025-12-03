
CREATE OR ALTER PROCEDURE from_OneN_to_OneOne (
    @TableOne   VARCHAR(100),
    @TableN     VARCHAR(100),
    @FKeyN      VARCHAR(100)
)
AS
BEGIN
    -- we need the PK's of the tables
    DECLARE @PK_One VARCHAR(100), @PK_N   VARCHAR(100);

    EXEC get_PK_name @TableOne, @PKColumn = @PK_One OUTPUT;
    EXEC get_PK_name @TableN, @PKColumn = @PK_N OUTPUT;

    -- we remove the row if the PK isn't max
    DECLARE @sql NVARCHAR(MAX);

    SET @sql = '
        DELETE t_N
        FROM ' + @TableN + ' t_N
        JOIN (
            SELECT ' + @FKeyN + ', MAX(' + @PK_N + ') AS KeepId
            FROM ' + @TableN + '
            WHERE ' + @FKeyN + ' IS NOT NULL
            GROUP BY ' + @FKeyN + '
        ) x ON t_N.' + @FKeyN + ' = x.' + @FKeyN + '
        WHERE t_N.' + @PK_N + ' != x.KeepId;
    ';
    PRINT(@sql); EXEC(@sql);

    -- add unique constraint to reinforce 1:1
    DECLARE @UQName VARCHAR(100) = 'UQ_' + @TableN + '_' + @FKeyN;

    SET @sql = '
        ALTER TABLE ' + @TableN + ' ADD CONSTRAINT ' + @UQName + ' UNIQUE (' + @FKeyN + ');
    ';
    PRINT(@sql); EXEC(@sql);

END;
GO