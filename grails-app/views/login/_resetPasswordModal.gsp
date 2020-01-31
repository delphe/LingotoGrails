<div class="modal fade" id="resetPasswordModal" tabindex="-1" role="dialog" aria-labelledby="resetPasswordModal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" data-dismiss="modal">
            <div class="modal-header">
            	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
               <g:message code="sendingResetLink.message" default="An email will be sent with a link to reset your password."/>
				<button id="sendEmail" name="sendEmail" class="btn btn-primary" style="width: 78px !important;">
			    	<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span><span class="sr-only">Send</span>&nbsp;
			    </button>
			    <button id="cancelEmail" name="cancelEmail" class="btn btn-danger" data-dismiss="modal" style="width: 78px !important;">
			    	<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span><span class="sr-only">Cancel</span>&nbsp;
			    </button>
            </div>
            <div class="modal-footer">

        </div>
    </div>
  </div>
</div>
