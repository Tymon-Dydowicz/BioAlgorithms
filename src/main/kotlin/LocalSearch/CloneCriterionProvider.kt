package LocalSearch

import org.slf4j.LoggerFactory
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class CloneCriterionProvider(private val prototype: IAcceptanceCriterion) {
    private val logger = LoggerFactory.getLogger(CloneCriterionProvider::class.java)
    private val constructor: KFunction<IAcceptanceCriterion> = prototype::class.constructors.firstOrNull()
            ?: error("No constructor found for ${prototype::class.simpleName}")
    private val constructorArgs: List<Any?> = extractConstructorArgs(this.prototype, constructor)

    init {
        val statefulDependencies = constructorArgs
            .filterIsInstance<Stateful>()
            .map { it::class.simpleName }

        logger.info(
            "CloneCriterionProvider initialized for ${prototype::class.simpleName} " +
                    "with stateful dependencies: ${statefulDependencies.joinToString(", ", prefix = "[", postfix = "]")}"
        )
    }

    fun create(): IAcceptanceCriterion {
        val clonedArgs = constructorArgs.map { arg ->
            when (arg) {
                is Stateful -> {
                    val clonedObject = arg.clone()
                    logger.info("Cloning Stateful parameter: ${clonedObject::class.simpleName} with ID: ${System.identityHashCode(clonedObject)}")
                    clonedObject
                }
                is Resettable -> {
                    logger.info("Resetting Resettable parameter: ${arg::class.simpleName} with ID: ${System.identityHashCode(arg)}")
                    arg.reset()
                }
                else -> {
                    logger.info("Using parameter: ${arg?.javaClass?.simpleName} with ID: ${System.identityHashCode(arg)}")
                    arg
                }
            }
        }

        return constructor.call(*clonedArgs.toTypedArray())
    }

    private fun extractConstructorArgs(obj: Any, constructor: KFunction<*>): List<Any?> {
        return constructor.parameters.map { param ->
            val property = obj::class.memberProperties
                .firstOrNull { it.name == param.name }
                ?: error("Cannot find property '${param.name}' in ${obj::class.simpleName}")

            property.isAccessible = true
            property.getter.call(obj)
        }
    }
}