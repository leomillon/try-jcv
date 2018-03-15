package web.demo.server.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import web.demo.server.common.GeneralPathsConstants
import web.demo.server.dtos.UserDto
import web.demo.server.dtos.course.Course
import web.demo.server.dtos.stepik.ProgressDto
import web.demo.server.exceptions.SourceNotFoundException
import web.demo.server.model.ProviderType
import web.demo.server.service.api.StepikService
import javax.servlet.http.HttpSession

/**
 * @author Alexander Prendota on 3/13/18 JetBrains.
 */
@RestController
@RequestMapping(GeneralPathsConstants.API_EDU)
class EducationController {

    @Autowired
    private lateinit var stepikService: StepikService

    /**
     * Getting personal course progress
     * Work only [ProviderType.stepik]
     * @param authentication - for getting token for request to stepik
     * @param session        - for getting info about user
     *
     * @throws [UnsupportedOperationException] - if provider is not [ProviderType.stepik]
     * @return list of [ProgressDto]
     */
    @GetMapping("${GeneralPathsConstants.PROGRESS}/{id}")
    fun getUserCourseProgress(authentication: OAuth2Authentication, session: HttpSession,
                              @PathVariable id: String): ResponseEntity<*> {
        val user = session.getAttribute(GeneralPathsConstants.CURRENT_USER) as UserDto
        if (user.provider != ProviderType.stepik.name)
            throw UnsupportedOperationException("Can not get course progress for ${user.provider} provider")
        val token = (authentication.details as OAuth2AuthenticationDetails).tokenValue
        val progress = stepikService.getCourseProgress(id, token)
        return ResponseEntity.ok(progress)
    }


    /**
     * Getting loaded Stepic courses.
     * See available course from application.yml
     */
    @GetMapping("${GeneralPathsConstants.COURSE}${GeneralPathsConstants.ALL}")
    fun getCourses(): ResponseEntity<*> {
        return ResponseEntity.ok(stepikService.getCourses())
    }

    /**
     * Getting [Course.id] and [Course.title]
     * @return list [Course] with only id and title
     */
    @GetMapping("${GeneralPathsConstants.COURSE}${GeneralPathsConstants.ALL}${GeneralPathsConstants.TITLE}")
    fun getCourseTitles(): ResponseEntity<*> {
        return ResponseEntity.ok(stepikService.getCoursesTitles())
    }

    /**
     * Getting full course by id
     *
     * @throws [SourceNotFoundException] - if course not found
     * @return [Course]
     */
    @GetMapping("${GeneralPathsConstants.COURSE}/{id}")
    fun getCourseById(@PathVariable id: String): ResponseEntity<*> {
        return ResponseEntity.ok(stepikService.getCourseById(id))
    }
}