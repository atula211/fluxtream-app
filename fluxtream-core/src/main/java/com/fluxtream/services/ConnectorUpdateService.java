package com.fluxtream.services;

import java.util.List;
import java.util.Set;

import com.fluxtream.connectors.Connector;
import com.fluxtream.connectors.updaters.AbstractUpdater;
import com.fluxtream.connectors.updaters.ScheduleResult;
import com.fluxtream.connectors.updaters.UpdateInfo;
import com.fluxtream.domain.ApiUpdate;
import com.fluxtream.domain.UpdateWorkerTask;

public interface ConnectorUpdateService {

    /**
     * Delete all scheduled tasks that are in progress
     */
	public void cleanupRunningUpdateTasks();

    /**
     * Schedules updates for the given connector for the user
     * @param guestId the user for whom the connector is to be updated
     * @param connector the connector to be updated
     * @return A list containing data about what was scheduled
     */
    public List<ScheduleResult> updateConnector(long guestId, Connector connector);

    /**
     * Schedules an updated for on ObjectType of the given connector for the given user
     * @param guestId The user for whom data is being collected
     * @param connector The connector that is being updated
     * @param objectTypes the objectType that is being updated. This is a bitmask which can represent multiple objectTypes
     *                    The value of each objectType is defined in the ObjectType spec. Values are always powers of 2
     *                    which allows for the bitmask. For example: objectTypes = 5 means that both the objectType of
     *                    value 4 and the objectType of value 1 are to be updated
     * @return A list containing data about what was scheduled
     */
    public List<ScheduleResult> updateConnectorObjectType(long guestId, Connector connector, int objectTypes);

    public List<ScheduleResult> updateAllConnectors(long guestId);

    public List<ApiUpdate> getUpdates(long guestId, Connector connector, int pageSize, int page);

	public void addUpdater(Connector connector, AbstractUpdater updater);

	public AbstractUpdater getUpdater(Connector connector);

	public ApiUpdate getLastUpdate(long guestId, Connector api);

	public ApiUpdate getLastSuccessfulUpdate(long guestId, Connector api);

	public ApiUpdate getLastSuccessfulUpdate(long guestId, Connector api,
			int objectTypes);

	public Set<Long> getConnectorGuests(Connector connector);
	
	public void addApiUpdate(long guestId, Connector api, int objectTypes,
			long ts, long elapsed, String query, boolean success);

	public void addApiNotification(Connector api, long guestId, String content);

	public ScheduleResult scheduleUpdate(long guestId, String connectorName,
			int objectTypes, UpdateInfo.UpdateType updateType,
			long timeScheduled, String... jsonParams);

	public UpdateWorkerTask getScheduledUpdateTask(long guestId, String connectorName, int objectTypes);

	public boolean isHistoryUpdateCompleted(long guestId, String connectorName,
			int objectTypes);

	public void pollScheduledUpdates();

    /**
     * Sets the updateWorkerTask to the given status
     * @param updateWorkerTaskId the id of the task whose status is to be updated
     * @param status the status to set the task to
     */
	public void setUpdateWorkerTaskStatus(long updateWorkerTaskId, UpdateWorkerTask.Status status);

	public ScheduleResult reScheduleUpdateTask(UpdateWorkerTask updateWorkerTask, long time,
                                               boolean incrementRetries, UpdateWorkerTask.AuditTrailEntry auditTrailEntry);

    /**
     * Returns a list of all scheduled updates for the connector for the given user
     * NOTE: If a tasks has been running for over 10 hours, this method will set that
     * tasks status to UpdateWorkerTask.Status.STALLED and will still return that result
     * @param guestId the user whose status is being retrieved
     * @param connector The connector for which the tasks are being retrieved
     * @return a list of scheduled tasks
     */
	public List<UpdateWorkerTask> getScheduledUpdateTasks(long guestId, Connector connector);

	public void deleteScheduledUpdateTasks(long guestId, Connector connector);

	public long getTotalNumberOfGuestsUsingConnector(Connector connector);

	public long getTotalNumberOfUpdates(Connector connector);

	public long getTotalNumberOfUpdatesSince(Connector connector, long then);

	public long getNumberOfUpdates(long guestId, Connector connector);

	public long getNumberOfUpdatesSince(long guestId, Connector connector,
			long then);
}
