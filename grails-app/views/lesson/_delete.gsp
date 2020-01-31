<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog"
	aria-labelledby="deleteModal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header"></div>
			<div class="modal-body">
				<div class="imgLink">
					<g:link controller="lesson" action="delete"
						params="[id:lesson?.id]">
						<img src="${resource(dir:'images',file:'trashit.jpg')}" alt=""
							border="0" />
					</g:link>
				</div>
				<div class="imgLink">
					<g:link controller="lesson" action="learn" params="[id:lesson?.id]">
						<img src="${resource(dir:'images',file:'notrash.jpg')}" alt=""
							border="0" />
					</g:link>
				</div>
			</div>
			<div class="modal-footer"></div>
		</div>
	</div>
</div>
