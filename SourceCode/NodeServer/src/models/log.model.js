export default function(sequelize, DataTypes) {
    return sequelize.define(
        "log", // Model Name
        {
            type: {
                type: DataTypes.STRING,
                allowNull: false
            },
            source: {
                type: DataTypes.STRING,
                allowNull: false
            },
            metadata: {
                type: DataTypes.STRING,
                allowNull: false
            },
            createdAt: {
                type: DataTypes.DATE,
                defaultValue: DataTypes.NOW
            },
            updatedAt: {
                type: DataTypes.DATE,
                defaultValue: DataTypes.NOW,
                onUpdate: DataTypes.NOW
            }
        }
    );
}
