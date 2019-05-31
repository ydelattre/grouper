<%@ include file="../assetsJsp/commonTaglib.jsp"%>

            <%-- for the new group or new stem button --%>
            <input type="hidden" name="objectStemId" value="${grouperRequestContainer.stemContainer.guiStem.stem.id}" />

            <%@ include file="stemHeader.jsp" %>

            <script language="javascript">
              $("#people-filter").on('change', function() {
                if (this.value == 'IMMEDIATE') {
                  $("#pit-filter").prop("selectedIndex", 0);
                } else if (this.value == 'NONIMMEDIATE') {
                  $("#enabled-disabled-filter").prop("selectedIndex", 0);
                  $("#pit-filter").prop("selectedIndex", 0);
                } else {
                  $("#enabled-disabled-filter").prop("selectedIndex", 0);
                }

                $("#pit-filter").trigger('change');
              });

              $("#enabled-disabled-filter").on('change', function() {
                if (this.value != 'nothing') {
                  $("#people-filter").prop("selectedIndex", 1);
                  $("#pit-filter").prop("selectedIndex", 0);
                }

                $("#pit-filter").trigger('change');
              });

              $("#custom-composite-filter").on('change', function() {
                if (this.value != 'nothing') {
                  $("#pit-filter").prop("selectedIndex", 0);
                }

                $("#pit-filter").trigger('change');
              });

              $("#pit-filter").on('change', function() {
                if (this.value == 'yes') {
                  $("#people-filter").prop("selectedIndex", 0);
                  $("#enabled-disabled-filter").prop("selectedIndex", 0);
                  $("#custom-composite-filter").prop("selectedIndex", 0);
                  $('.groupMembersAdvancedPITShow').show('slow');
                } else {
                  $('.groupMembersAdvancedPITShow').hide('slow');
                }
              });
            </script>

            <div class="row-fluid">
              <div class="span12 tab-interface">
                <ul class="nav nav-tabs">
                  <li><a role="tab" href="#" onclick="return guiV2link('operation=UiV2Stem.viewStem&stemId=${grouperRequestContainer.stemContainer.guiStem.stem.id}', {dontScrollTop: true});" >${textContainer.text['stemContents'] }</a></li>
                  <c:if test="${grouperRequestContainer.stemContainer.canAdminPrivileges}">
                    <li><a role="tab" href="#" onclick="return guiV2link('operation=UiV2Stem.stemPrivileges&stemId=${grouperRequestContainer.stemContainer.guiStem.stem.id}', {dontScrollTop: true});" >${textContainer.text['stemPrivileges'] }</a></li>
                  </c:if>
                  <%@ include file="stemMoreTab.jsp" %>
                </ul>
                <p class="lead">${textContainer.text['stemGroupMembershipsInFolderDecription'] }</p>
                <form class="form-inline form-small form-filter" id="groupFilterFormId">
                  <div class="row-fluid">
                    <div class="span1" id="groupFilterForDiv">
                      <label for="people-filter">${textContainer.text['groupFilterFor'] }</label>
                    </div>
                    <div class="span4">
                      <select id="people-filter" name="membershipType">
                        <option value="">${textContainer.text['groupFilterAllAssignments']}</option>
                        <option value="IMMEDIATE">${textContainer.text['groupFilterDirectAssignments']}</option>
                        <option value="NONIMMEDIATE">${textContainer.text['groupFilterIndirectAssignments']}</option>
                      </select>
                    </div>
                    <div class="span3" id="groupFilterTextDiv">
                      <input type="text" placeholder="${textContainer.textEscapeXml['groupFilterFormPlaceholder']}" 
                         name="filterText" id="table-filter" class="span12"/>
                    </div>
    
                    <div class="span4" id="groupFilterSubmitDiv"><input type="submit" class="btn" aria-controls="groupFilterResultsId" id="filterSubmitId" value="${textContainer.textEscapeDouble['groupApplyFilterButton'] }"
                        onclick="ajax('../app/UiV2Stem.groupMembershipsInFolderFilter?stemId=${grouperRequestContainer.stemContainer.guiStem.stem.id}', {formIds: 'groupFilterFormId,groupPagingFormId'}); return false;"> 
                      <a class="btn" role="button" onclick="$('#people-filter').val(''); $('#custom-composite-filter').val('nothing'); $('#enabled-disabled-filter').val('nothing'); $('#pit-filter').val('no'); $('#pit-filter').trigger('change'); $('#table-filter').val(''); $('#filterSubmitId').click(); return false;">${textContainer.text['groupResetButton'] }</a>
                      <a role="button" class="btn groupMembersAdvancedHide" onclick="$('#groupFilterForDiv').removeClass('span1').addClass('span2'); $('#groupFilterTextDiv').appendTo('#groupFilterAdvancedFilterTextRowDiv'); $('#groupFilterSubmitDiv').appendTo('#groupFilterAdvancedButtonRowDiv'); $('.groupMembersAdvancedShow').show('slow'); $('.groupMembersAdvancedHide').hide('slow'); return false;">${textContainer.text['groupAdvancedButton'] }</a>
                    </div>
                       
                  </div>
                  <div class="row-fluid groupMembersAdvancedShow" style="margin-top: 5px; display: none;" id="groupFilterAdvancedFilterTextRowDiv">
                    <div class="span2">&nbsp;</div>
                  </div>
                  <c:if test="${grouperRequestContainer.stemContainer.customCompositeUiKeys != null && grouperRequestContainer.stemContainer.customCompositeUiKeys.size() > 0}">
                    <div class="row-fluid groupMembersAdvancedShow" style="margin-top: 5px; display: none;">
                      <div class="span2">
                        <label for="custom-composite-filter">${textContainer.text['groupFilterCustomCompositeOptions']}</label>
                      </div>
                      <div class="span4">
                        <select id="custom-composite-filter" name="membershipCustomCompositeOptions">
                          <option value="nothing"></option>
                          <c:forEach items="${grouperRequestContainer.stemContainer.customCompositeUiKeys.entrySet()}" var="customCompositeEntry">
                            <option value="${customCompositeEntry.getKey()}">${textContainer.text[customCompositeEntry.getValue()]}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>
                  </c:if>
                  <div class="row-fluid groupMembersAdvancedShow" style="margin-top: 5px; display: none;">
                    <div class="span2">
                      <label for="enabled-disabled-filter">${textContainer.text['groupFilterEnabledDisabledOptions'] }</label>
                    </div>
                    <div class="span4">
                      <select id="enabled-disabled-filter" name="membershipEnabledDisabledOptions">
                        <option value="nothing"></option>
                        <option value="status">${textContainer.text['groupFilterEnabledDisabledOptionsStatus']}</option>
                        <option value="disabled_dates">${textContainer.text['groupFilterEnabledDisabledOptionsDisabledDates']}</option>
                        <option value="enabled_dates">${textContainer.text['groupFilterEnabledDisabledOptionsEnabledDates']}</option>
                      </select>
                    </div>
                  </div>
                  <div class="row-fluid groupMembersAdvancedShow" style="margin-top: 5px; display: none;">
                    <div class="span2">
                      <label for="pit-filter">${textContainer.text['groupFilterPITOptions'] }</label>
                    </div>
                    <div class="span4">
                      <select id="pit-filter" name="membershipPITOptions">
                        <option value="no">${textContainer.text['groupFilterPITNo']}</option>
                        <option value="yes">${textContainer.text['groupFilterPITYes']}</option>
                      </select>
                    </div>
                  </div>
                  <div class="row-fluid groupMembersAdvancedPITShow" style="margin-top: 5px; display: none;">
                    <div class="span2">
                      <label for="pit-from-date-filter">${textContainer.text['groupFilterPITFromDate'] }</label>
                    </div>
                    <div class="span3">
                      <input type="text" placeholder="YYYY/MM/DD" name="membershipPITFromDate" id="pit-from-date-filter" class="span12"/>
                    </div>
                  </div>
                  <div class="row-fluid groupMembersAdvancedPITShow" style="margin-top: 5px; display: none;">
                    <div class="span2">
                      <label for="pit-to-date-filter">${textContainer.text['groupFilterPITToDate'] }</label>
                    </div>
                    <div class="span3">
                      <input type="text" placeholder="YYYY/MM/DD" name="membershipPITToDate" id="pit-to-date-filter" class="span12"/>
                    </div>
                  </div>
                  <div class="row-fluid groupMembersAdvancedShow" style="margin-top: 5px; display: none;" id="groupFilterAdvancedButtonRowDiv">
                    <div class="span2">&nbsp;</div>
                  </div>
                </form>
                <script>
                  //set this flag so we get one confirm message on this screen
                  confirmedChanges = false;
                </script>
                <div id="groupMembershipsInFolderResultsId" role="region" aria-live="polite">
              </div>                
            </div>
          </div>