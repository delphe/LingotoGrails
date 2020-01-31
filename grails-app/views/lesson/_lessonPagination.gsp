<div class="text-center">
	<ul class="pagination pagination-lg">
		<li>
			<g:if test="${filteredLessonByAuthorList && filteredLessonByAuthorList?.size()>0 && filteredLessonByAuthorList[0]?.toInteger() != lesson?.id?.toInteger()}">
					<g:link controller="lesson" action="lessonPlan"
						params="[previous:true,id:lesson?.id,filteredLessonByAuthorList:filteredLessonByAuthorList,
								beginner:params.beginner,
								intermediate:params.intermediate,
								advanced:params.advanced,
								formal:params.formal,
								informal:params.informal]">
						<span aria-hidden="true">&laquo;</span><span class="sr-only">Previous</span>
					</g:link>
				</g:if>
		</li>
		<li>
			
				<g:paginate action="learn"
					total="${filteredLessonByAuthorList?.size()?:0}" max="1"
					omitPrev="true" omitNext="true" 
					params="[beginner:params.beginner,
							intermediate:params.intermediate,
							advanced:params.advanced,
							formal:params.formal,
							informal:params.informal]"/>
			
		</li>
		<li>
			<g:if test="${filteredLessonByAuthorList && filteredLessonByAuthorList?.size()>0}">
				<g:if test="${session.credits <= 0 && !viewedLesson?.translation}">
						<g:link controller="lesson" action="lessonPlan" class="hideit unlocked"
							params="[next:true,id:lesson?.id,filteredLessonByAuthorList:filteredLessonByAuthorList,
								beginner:params.beginner,
								intermediate:params.intermediate,
								advanced:params.advanced,
								formal:params.formal,
								informal:params.informal]">
							<span aria-hidden="true" class="step">&raquo;</span><span class="sr-only">Next</span>
						</g:link>
					<span id="locked" aria-hidden="true" class="step red">&raquo;</span>
				</g:if> <g:else>
					<g:link controller="lesson" action="lessonPlan"
						params="[next:true,id:lesson?.id,filteredLessonByAuthorList:filteredLessonByAuthorList,
								beginner:params.beginner,
								intermediate:params.intermediate,
								advanced:params.advanced,
								formal:params.formal,
								informal:params.informal]">
						<span aria-hidden="true" class="step">&raquo;</span><span class="sr-only">Next</span>
					</g:link>
				</g:else>
			</g:if>
		</li>
	</ul>
</div>