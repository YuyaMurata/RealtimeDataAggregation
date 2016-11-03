-- -----------------------------
-- create table of ranksys
-- -----------------------------

-- <!--UserAgent Entity define-->
-- <entity type="aggregateagent">
--      <attribute name="AgentID" type="string" primarykey="true" maxlength="32"/>
--      <attribute name="Condition" type="aggregatecondition" singleentity="true"/>
--	<attribute name="Data" type="long" />
--	<attribute name="ConnectionCount" type="long" />
--      <attribute name="MessageLatency" type="long" />
--      <attribute name="MessageQueueLength" type="long" />
--	<attribute name="Log" type="log" />
--</entity>
--<entity type="aggregatecondition">
--      <attribute name="AgentID" type="string" primarykey="true" relationto="AgentID" maxlength="32"/>
--	<attribute name="Conditions" type="string" maxlength="32"/>
--	<attribute name="LastAccessTime" type="timestamp" />
--</entity>
--<entity type="log">
--	<attribute name="AgentID" type="string" primarykey="true" relationto="AgentID" maxlength="32"/>
--	<attribute name="AccessID" type="string" primarykey="true" maxlength="16"/>
--	<attribute name="CurrentTime" type="long"/>
--      <attribute name="LastAccessTime" type="timestamp" />
--</entity>

CREATE TRANSIENT TABLE aggregateagent (
    AgentID VARCHAR(64) NOT NULL,
    Data BIGINT,
    ConnectionCount BIGINT,
    MessageLatency BIGINT,
    MessageQueueLength BIGINT,
    PRIMARY KEY(AgentID)
);
COMMIT WORK;
