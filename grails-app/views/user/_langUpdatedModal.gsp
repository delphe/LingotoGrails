<script type="text/javascript">
    $(window).load(function(){
        $('#langUpdatedModal').modal('show');
    });
</script>

<div class="modal fade" id="langUpdatedModal" tabindex="-1" role="dialog" 
	aria-labelledby="langUpdatedModal" aria-hidden="true" data-keyboard="false" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
            <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            <div class="modal-body">
                <div id="langUpdatedMsg" class="errors">
					<g:message code="language.change.warning.message" default="Warning: You have made changes to your language." />
					<br/><br/>
					<g:message code="language.change.all.message" default="Change the language on all previous and future lessons" />:
					<g:radio name="lessonsToChange" value="allLangs" />
					<br/>
					<g:message code="language.change.future.message" default="Change the language on future lessons only" />:
					<g:radio name="lessonsToChange" value="futureLangs"
						checked="checked" />
					<br/><br/>
					<button id="updateButton" name="updateButton" class="save btn btn-primary" form='updateLang' style="width: 78px !important;">
				    	<span class="glyphicon glyphicon-save" aria-hidden="true"></span><span class="sr-only">Save</span>&nbsp;
				    </button>
				</div>
            </div>
            <div class="modal-footer">

        </div>
    </div>
  </div>
</div>